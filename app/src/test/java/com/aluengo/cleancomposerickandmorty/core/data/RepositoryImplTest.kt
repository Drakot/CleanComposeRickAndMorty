package com.aluengo.cleancomposerickandmorty.core.data

import com.aluengo.cleancomposerickandmorty.BaseTest
import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.data.local.LocalStorage
import com.aluengo.cleancomposerickandmorty.core.data.remote.ApiService
import com.aluengo.cleancomposerickandmorty.core.domain.Repository
import com.aluengo.cleancomposerickandmorty.listCharacters.data.MockData
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharacterRequest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class RepositoryImplTest : BaseTest() {
    private lateinit var mockData: MockData
    private lateinit var sut: Repository
    lateinit var api: ApiService
    lateinit var db: LocalStorage


    @Before
    fun setUp() {
        api = mockk()
        db = mockk()
        sut = RepositoryImpl(api, db, Mapper())
        mockData = MockData(Mapper())
    }

    @Test
    fun `listCharacters should return characters from API when DB is empty`() = runTest {
        val request = ListCharacterRequest("someFilter")
        val charactersFromApi = mockData.createListCharactersResponse()
        val expectedApiData = Resource.Success(charactersFromApi)
        val expectedResource = mockData.createListCharactersDomain()
        val localTestData = mockData.createListCharactersLocal()
        val infoData = mockData.createListCharactersInfoLocal()

        coEvery { db.getCharacters(request.filter) } returnsMany listOf(flowOf(emptyList()), flowOf(localTestData))
        coEvery { db.getInfo() } returnsMany listOf(flowOf(null), flowOf(infoData))

        coEvery { db.saveCharacters(any()) } just runs
        coEvery { db.saveCharactersPagination(any()) } just runs
        coEvery { api.listCharacters(request) } returns expectedApiData

        val result= sut.listCharacters(request).first()
        assertEquals(expectedResource, (result as Resource.Success).data)
    }

    @Test
    fun `listCharacters should return characters from DB when available`() = runTest {
        val request = ListCharacterRequest("someFilter")
        val charactersFromApi = mockData.createListCharactersResponse()
        val expectedApiData = Resource.Success(charactersFromApi)
        val expectedResource = mockData.createListCharactersDomain()
        val localTestData = mockData.createListCharactersLocal()
        val infoData = mockData.createListCharactersInfoLocal()

        coEvery { db.getCharacters(request.filter) } returnsMany listOf(flowOf(localTestData), flowOf(localTestData))
        coEvery { db.getInfo() } returnsMany listOf(flowOf(infoData), flowOf(infoData))
        coEvery { db.saveCharacters(any()) } just runs
        coEvery { db.saveCharactersPagination(any()) } just runs
        coEvery { api.listCharacters(request) } returns expectedApiData

        val result= sut.listCharacters(request).first()
        assertEquals(expectedResource, (result as Resource.Success).data)
    }

    @Test
    fun `listCharacters with Resource_Error should emit error`() = runTest {
        val error = ErrorResponse(0, ErrorData("", ErrorType.NotConnected))
        val request = ListCharacterRequest("someFilter")

        coEvery { api.listCharacters(request) } returns Resource.Error(error)
        coEvery { db.getCharacters(request.filter) } returnsMany listOf(flowOf(emptyList()), flowOf(emptyList()))
        coEvery { db.getInfo() } returnsMany listOf(flowOf(null), flowOf(null))

        val result = sut.listCharacters(request).first()

        coVerify(exactly = 1) { api.listCharacters(request) }

        TestCase.assertTrue(result is Resource.Error)
        assertEquals(error, (result as Resource.Error).error)
    }

    @Test
    fun `listCharactersWithFilter should return characters from API`() = runTest {
        val request = ListCharacterRequest("someFilter")
        val charactersFromApi = mockData.createListCharactersResponse()
        val expectedApiData = Resource.Success(charactersFromApi)
        val expectedResource = mockData.createListCharactersDomain()

        coEvery { api.listCharacters(request) } returns expectedApiData

        val result = sut.listCharactersWithFilter(request).first()

        coVerify(exactly = 1) { api.listCharacters(request) }

        TestCase.assertTrue(result is Resource.Success)
        assertEquals(expectedResource, (result as Resource.Success).data)
    }

    @Test
    fun `listCharactersWithFilter with Resource_Error should emit error`() = runTest {
        val error = ErrorResponse(0, ErrorData("", ErrorType.NotConnected))
        val request = ListCharacterRequest("someFilter")

        coEvery { api.listCharacters(request) } returns Resource.Error(error)

        val result = sut.listCharactersWithFilter(request).first()

        coVerify(exactly = 1) { api.listCharacters(request) }

        TestCase.assertTrue(result is Resource.Error)
        assertEquals(error, (result as Resource.Error).error)
    }


    @Test
    fun `Get Character should return character from API when DB is empty`() = runTest {
        val request = 1
        val charactersFromApi = mockData.createListCharactersResponse().results.first()
        val expectedApiData = Resource.Success(charactersFromApi)
        val expectedResource = mockData.createListCharactersDomain().results.first()
        val localTestData = mockData.createListCharactersLocal().first()

        coEvery { db.getCharacter(request) } returns flowOf(null)

        coEvery { db.saveCharacter(any()) } just runs
        coEvery { api.getCharacter(request) } returns expectedApiData

        val result = sut.getCharacter(request).first()
        assertEquals(expectedResource, (result as Resource.Success).data)
    }

    @Test
    fun `Get Character should return character first from DB then from Api when available`() = runTest {
        val request = 1
        val charactersFromApi = mockData.createListCharactersResponse().results[1]
        val expectedApiData = Resource.Success(charactersFromApi)
        val expectedResource = mockData.createListCharactersDomain().results.first()
        val localTestData = mockData.createListCharactersLocal().first()

        coEvery { db.getCharacter(request) } returns flowOf(localTestData)
        coEvery { db.saveCharacter(any()) } just runs
        coEvery { api.getCharacter(request) } returns expectedApiData

        val result = sut.getCharacter(request).first()
        assertEquals(expectedResource, (result as Resource.Success).data)
    }

    @Test
    fun `Get Character with Resource_Error should emit error`() = runTest {
        val error = ErrorResponse(0, ErrorData("", ErrorType.NotConnected))
        val request = 1

        coEvery { api.getCharacter(request) } returns Resource.Error(error)
        coEvery { db.getCharacter(request) } returns flowOf(null)

        val result = sut.getCharacter(request).first()

        coVerify(exactly = 1) { api.getCharacter(request) }

        TestCase.assertTrue(result is Resource.Error)
        assertEquals(error, (result as Resource.Error).error)
    }

}