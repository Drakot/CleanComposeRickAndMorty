package com.aluengo.cleancomposerickandmorty.detailCharacter.domain

import com.aluengo.cleancomposerickandmorty.BaseTest
import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.data.ErrorData
import com.aluengo.cleancomposerickandmorty.core.data.ErrorResponse
import com.aluengo.cleancomposerickandmorty.core.data.ErrorType
import com.aluengo.cleancomposerickandmorty.core.data.Mapper
import com.aluengo.cleancomposerickandmorty.core.domain.Repository
import com.aluengo.cleancomposerickandmorty.listCharacters.data.MockData
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersDomain
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCharacterUseCaseTest : BaseTest() {
    private lateinit var mockData: MockData
    private lateinit var sut: GetCharacterUseCase
    private lateinit var repository: Repository

    @Before
    fun setup() {
        repository = mockk()
        sut = GetCharacterUseCase(repository)
        mockData = MockData(Mapper())
    }

    @Test
    fun `invoke should return a character from repository`() = runTest {
        val id = 1
        val characters = mockData.createListCharactersDomain().results.first()
        val expectedResource = Resource.Success(characters)
        coEvery { repository.getCharacter(id) } returns flowOf(expectedResource)

        val result: Flow<Resource<ListCharactersDomain.Result>> = sut(id)

        assertEquals(expectedResource, result.single())
    }

    @Test
    fun `invoke should return result error`() = runTest {
        val error = ErrorResponse(0, ErrorData("", ErrorType.Default))
        val id = 1
        coEvery { repository.getCharacter(id) } returns flowOf(Resource.Error(error))

        val result = sut(id).first()

        assertTrue(result is Resource.Error)
        assertEquals(error, (result as Resource.Error).error)
    }

}