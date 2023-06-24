package com.aluengo.cleancomposerickandmorty.listCharacters.domain

import com.aluengo.cleancomposerickandmorty.BaseTest
import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.data.ErrorData
import com.aluengo.cleancomposerickandmorty.core.data.ErrorResponse
import com.aluengo.cleancomposerickandmorty.core.data.ErrorType
import com.aluengo.cleancomposerickandmorty.core.data.Mapper
import com.aluengo.cleancomposerickandmorty.core.domain.Repository
import com.aluengo.cleancomposerickandmorty.listCharacters.data.MockData
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ListCharactersUseCaseTest : BaseTest() {
    private lateinit var mockData: MockData
    private lateinit var repository: Repository
    private lateinit var useCase: ListCharactersUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = ListCharactersUseCase(repository)
        mockData = MockData(Mapper())
    }

    @Test
    fun `invoke should return repository's listCharacters result success`() = runTest {
        val request = ListCharacterRequest("someFilter")
        val charactersDomain = mockData.createListCharactersDomain()
        val resourceSuccess = Resource.Success(charactersDomain)

        coEvery { repository.listCharacters(request) } returns flowOf(resourceSuccess)

        val result = useCase(request).first()

        assertTrue(result is Resource.Success)
        assertEquals(charactersDomain, (result as Resource.Success).data)
    }

    @Test
    fun `invoke should return repository's listCharacters result error`() = runTest {
        val request = ListCharacterRequest("someFilter")
        val error = ErrorResponse(0, ErrorData("", ErrorType.Default))

        coEvery { repository.listCharacters(request) } returns flowOf(Resource.Error(error))

        val result = useCase(request).first()

        assertTrue(result is Resource.Error)
        assertEquals(error, (result as Resource.Error).error)
    }

}