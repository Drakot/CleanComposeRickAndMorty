package com.aluengo.cleancomposerickandmorty.listCharacters.presentation


import com.aluengo.cleancomposerickandmorty.BaseTest
import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.data.ErrorData
import com.aluengo.cleancomposerickandmorty.core.data.ErrorResponse
import com.aluengo.cleancomposerickandmorty.core.data.ErrorType
import com.aluengo.cleancomposerickandmorty.core.ui.SearchWidgetState
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharacterRequest
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersDomain
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersUseCase
import com.aluengo.cleancomposerickandmorty.testFlow
import com.google.common.truth.Truth.assertThat
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListCharactersViewModelTest : BaseTest() {
    lateinit var listUseCase: ListCharactersUseCase
    private lateinit var sut: ListCharactersViewModel

    @Before
    fun setUp() {
        listUseCase = mockk()
        sut = spyk(ListCharactersViewModel(listUseCase))
    }

    @Test
    fun `on init should load the list characters`(): Unit = runTest {
        val request = ListCharacterRequest("", 1)
        val testData = createTestData()
        val uiTestData = ListCharactersUI.fromDomain(testData)
        sut.submitIntent(ListCharactersIntent.Load)
        coEvery { listUseCase(request) } returns flow {
            emit(Resource.Success(testData))
        }

        val viewState = sut.viewState
        assertThat(viewState.value.data).isEqualTo(listOf<ListCharactersUI.Result>())
        assertThat(true).isEqualTo(viewState.value.isLoading)

        viewState.testFlow(this) {
            assertThat(viewState.value.isLoading).isEqualTo(false)
            assertThat(viewState.value.data).isEqualTo(uiTestData.results)
        }
    }

    @Test
    fun `on search should update search text in view state`() {

        val searchText = "Search Text"

        sut.submitIntent(ListCharactersIntent.OnTypeSearch(searchText))

        assertThat(sut.viewState.value.searchText).isEqualTo(searchText)
    }

    @Test
    fun `on close search click should update search state and call get characters`() {
        val initialState = sut.viewState.value

        sut.submitIntent(ListCharactersIntent.OnCloseSearchClick)

        val finalState = sut.viewState.value
        assertThat(finalState.searchState).isEqualTo(SearchWidgetState.CLOSED)
        assertThat(finalState).isNotEqualTo(initialState)
    }

    @Test
    fun `end of list reached should call get characters`() = runTest {
        val viewState = sut.viewState
        val currentPage = 1
        sut.pageInfo = ListCharactersUI.PageInfo(currentPage, lastPage = false)
        val request = ListCharacterRequest("", currentPage + 1)
        val testData = createTestData()

        coEvery { listUseCase(request) } returns flow {
            emit(Resource.Success(testData))
        }

        initialState()

        sut.submitIntent(ListCharactersIntent.EndOfListReached)

        coEvery { listUseCase(mockk()) }
        viewState.testFlow(this) {
            assertThat(viewState.value.isLoading).isEqualTo(false)
            assertThat(viewState.value.data).isEqualTo(ListCharactersUI.fromDomain(testData).results)
        }
    }

    @Test
    fun `end of list reached should not call get characters when loading`() = runTest {
        initialState()
        sut.submitState(sut.viewState.value.copy(isLoading = true))
        sut.pageInfo = ListCharactersUI.PageInfo(currentPage = 1, lastPage = false)
        sut.submitIntent(ListCharactersIntent.EndOfListReached)

        assertThat(sut.viewState.value.isLoading).isEqualTo(true)

        verify { listUseCase wasNot Called }
    }

    @Test
    fun `end of list reached should not call get characters when already on last page`() = runTest {
        sut.pageInfo = ListCharactersUI.PageInfo(currentPage = 1, lastPage = true)

        sut.submitIntent(ListCharactersIntent.EndOfListReached)

        assertThat(sut.viewState.value.isLoading).isEqualTo(false)
        verify { listUseCase wasNot Called }
    }

    @Test
    fun `on search should update search state, reset page info, and call get characters`() = runTest {
        initialState()
        val viewState = sut.viewState
        val searchText = "Search Text"
        sut.submitIntent(ListCharactersIntent.OnTypeSearch(searchText))
        sut.pageInfo = ListCharactersUI.PageInfo(currentPage = 2, lastPage = false)

        val request = ListCharacterRequest(searchText, 1)
        val testData = createTestData()

        coEvery { listUseCase(request) } returns flow {
            emit(Resource.Success(testData))
        }

        sut.submitIntent(ListCharactersIntent.OnSearch(searchText))

        assertThat(viewState.value.searchText).isEqualTo(searchText)
        assertThat(sut.pageInfo?.currentPage).isEqualTo(1)
        assertThat(sut.pageInfo?.lastPage).isEqualTo(false)
        assertThat(viewState.value.data).isEqualTo(ListCharactersUI.fromDomain(testData).results)
    }

    @Test
    fun `on search clicked should update search state to opened`() {
        val initialState = sut.viewState.value

        sut.submitIntent(ListCharactersIntent.OnSearchClicked)

        val finalState = sut.viewState.value
        assertThat(finalState.searchState).isEqualTo(SearchWidgetState.OPENED)
        assertThat(finalState).isNotEqualTo(initialState)
    }

    @Test
    fun `callGetCharacters should handle Resource Error and show error event`() = runTest {
        val request = ListCharacterRequest("", 1)
        val error = ErrorResponse(0, ErrorData("", ErrorType.NotConnected))

        coEvery { listUseCase(request) } returns flow {
            emit(Resource.Error(error))
        }

        sut.submitIntent(ListCharactersIntent.Load)
        sut.viewState.testFlow(this) {
            verify { sut.submitSingleEvent(match { it is ListCharactersUiSingleEvent.ShowError && it.errorType == ErrorType.NotConnected }) }
        }
    }


    fun initialState() {
        val viewState = sut.viewState
        val testData = createTestData()
        sut.submitState(
            viewState.value.copy(data = ListCharactersUI.fromDomain(testData).results)
        )
    }

    fun createTestData(): ListCharactersDomain {
        val info = ListCharactersDomain.Info(
            count = 2,
            next = "https://example.com/next?page=2",
            pages = 3,
            prev = null
        )

        val result1 = ListCharactersDomain.Result(
            id = 1,
            gender = "Male",
            image = "https://example.com/image1.jpg",
            name = "John Doe",
            species = "Human",
            status = "Alive",
            type = "Type 1",
            url = "https://example.com/character/1"
        )

        val result2 = ListCharactersDomain.Result(
            id = 2,
            gender = "Female",
            image = "https://example.com/image2.jpg",
            name = "Jane Smith",
            species = "Alien",
            status = "Dead",
            type = "Type 2",
            url = "https://example.com/character/2"
        )

        return ListCharactersDomain(info = info, results = listOf(result1, result2))
    }
}