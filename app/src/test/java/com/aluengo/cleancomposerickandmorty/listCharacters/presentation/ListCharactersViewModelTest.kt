package com.aluengo.cleancomposerickandmorty.listCharacters.presentation


import com.aluengo.cleancomposerickandmorty.BaseTest
import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.data.ErrorData
import com.aluengo.cleancomposerickandmorty.core.data.ErrorResponse
import com.aluengo.cleancomposerickandmorty.core.data.ErrorType
import com.aluengo.cleancomposerickandmorty.core.data.Mapper
import com.aluengo.cleancomposerickandmorty.core.ui.SearchWidgetState
import com.aluengo.cleancomposerickandmorty.listCharacters.data.MockData
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharacterRequest
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersDomain
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersUseCase
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListFilteredCharactersUseCase
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
    private lateinit var domainTestData: ListCharactersDomain
    lateinit var listUseCase: ListCharactersUseCase
    lateinit var listFilteredCharactersUseCase: ListFilteredCharactersUseCase
    private lateinit var sut: ListCharactersViewModel
    private lateinit var mockData: MockData

    @Before
    fun setUp() {
        listUseCase = mockk()
        listFilteredCharactersUseCase = mockk()
        sut = spyk(ListCharactersViewModel(listUseCase,listFilteredCharactersUseCase))
        mockData = MockData(Mapper())
        domainTestData = mockData.createListCharactersDomain()
    }

    @Test
    fun `on init should load the list characters`(): Unit = runTest {
        val request = ListCharacterRequest("", 1)

        val uiTestData = ListCharactersUI.fromDomain(domainTestData)
        sut.submitIntent(ListCharactersIntent.Load)
        coEvery { listUseCase(request) } returns flow {
            emit(Resource.Success(domainTestData))
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

        coEvery { listUseCase(request) } returns flow {
            emit(Resource.Success(domainTestData))
        }

        initialState()

        sut.submitIntent(ListCharactersIntent.EndOfListReached)

        coEvery { listUseCase(mockk()) }
        viewState.testFlow(this) {
            assertThat(viewState.value.isLoading).isEqualTo(false)
            assertThat(viewState.value.data).isEqualTo(ListCharactersUI.fromDomain(domainTestData).results)
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

        coEvery { listFilteredCharactersUseCase(request) } returns flow {
            emit(Resource.Success(domainTestData))
        }

        sut.submitIntent(ListCharactersIntent.OnSearch(searchText))

        assertThat(viewState.value.searchText).isEqualTo(searchText)
        assertThat(sut.pageInfo?.currentPage).isEqualTo(1)
        assertThat(sut.pageInfo?.lastPage).isEqualTo(false)
        assertThat(viewState.value.data).isEqualTo(ListCharactersUI.fromDomain(domainTestData).results)
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

    @Test
    fun `callGetCharacters with filter should handle Resource Error and show error event`() = runTest {
        initialState()
        val searchText = "Search Text"
        sut.submitIntent(ListCharactersIntent.OnTypeSearch(searchText))
        sut.pageInfo = ListCharactersUI.PageInfo(currentPage = 2, lastPage = false)

        val request = ListCharacterRequest(searchText, 1)
        val error = ErrorResponse(0, ErrorData("", ErrorType.NotConnected))

        coEvery { listFilteredCharactersUseCase(request) } returns flow {
            emit(Resource.Error(error))
        }

        sut.submitIntent(ListCharactersIntent.Load)
        sut.viewState.testFlow(this) {
            verify { sut.submitSingleEvent(match { it is ListCharactersUiSingleEvent.ShowError && it.errorType == ErrorType.NotConnected }) }
        }
    }


    fun initialState() {
        val viewState = sut.viewState
        sut.submitState(
            viewState.value.copy(data = ListCharactersUI.fromDomain(domainTestData).results)
        )
    }
}