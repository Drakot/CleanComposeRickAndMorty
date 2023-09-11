package com.aluengo.cleancomposerickandmorty.detailCharacter.presentation

import app.cash.turbine.test
import com.aluengo.cleancomposerickandmorty.BaseTest
import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.data.ErrorData
import com.aluengo.cleancomposerickandmorty.core.data.ErrorResponse
import com.aluengo.cleancomposerickandmorty.core.data.ErrorType
import com.aluengo.cleancomposerickandmorty.core.data.Mapper
import com.aluengo.cleancomposerickandmorty.core.navigation.CharacterItemInput
import com.aluengo.cleancomposerickandmorty.detailCharacter.domain.GetCharacterUseCase
import com.aluengo.cleancomposerickandmorty.listCharacters.data.MockData
import com.aluengo.cleancomposerickandmorty.listCharacters.presentation.ListCharactersUI
import com.aluengo.cleancomposerickandmorty.testFlow
import com.google.common.truth.ExpectFailure.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterViewModelTest : BaseTest() {
    private lateinit var mockData: MockData
    private lateinit var sut: CharacterViewModel
    private lateinit var mockGetCharacterUseCase: GetCharacterUseCase


    @Before
    fun setup() {
        mockGetCharacterUseCase = mockk()
        sut = spyk(CharacterViewModel(mockGetCharacterUseCase))
        mockData = MockData(Mapper())
    }

    @Test
    fun `submitIntent Load should call getCharacterUseCase and update state with success`() =
        runTest {
            val characterId = 1
            val characterIntent =
                CharacterIntent.Load(CharacterItemInput(characterId, "Test Name"))
            val characterDomain = mockData.createListCharactersDomain().results.first()
            val expectedData = ListCharactersUI.fromDomain(characterDomain)

            coEvery { mockGetCharacterUseCase(characterId) } returns flow {
                emit(Resource.Success(characterDomain))
            }

            assertEquals(false, sut.viewState.value.isLoading)
            sut.submitIntent(characterIntent)
            sut.viewState.test {
                assertEquals(true, awaitItem().isLoading)
                assertEquals(expectedData, awaitItem().data)
                assertEquals(false, awaitItem().isLoading)
            }
        }

    @Test
    fun `submitIntent Load should call getCharacterUseCase and update state with error`() =
        runTest {
            val characterId = 1
            val characterIntent = CharacterIntent.Load(CharacterItemInput(characterId, "Test Name"))
            val error = ErrorResponse(0, ErrorData("", ErrorType.NotConnected))

            coEvery { mockGetCharacterUseCase(characterId) } returns flow {
                emit(Resource.Error(error))
            }

            sut.submitIntent(characterIntent)
            sut.viewState.testFlow(this) {
                verify { sut.submitSingleEvent(match { it is CharacterUiSingleEvent.ShowError && it.errorType == ErrorType.NotConnected }) }
            }
        }

    @Test
    fun `submitIntent OnClickNavIcon should submit GoBack event`() = runTest {
        val characterIntent = CharacterIntent.OnClickNavIcon

        sut.submitIntent(characterIntent)

        verify(exactly = 1) { sut.submitSingleEvent(CharacterUiSingleEvent.GoBack) }
    }
}