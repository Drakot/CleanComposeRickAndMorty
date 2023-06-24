package com.aluengo.cleancomposerickandmorty.core.data.remote

import com.aluengo.cleancomposerickandmorty.BaseTest
import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.data.Mapper
import com.aluengo.cleancomposerickandmorty.listCharacters.data.ListCharactersResponse
import com.aluengo.cleancomposerickandmorty.listCharacters.data.MockData
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharacterRequest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class ApiServiceTest: BaseTest() {

    private lateinit var mockData: MockData
    private lateinit var apiService: ApiService
    private lateinit var mockApiClient: ApiClient

    @Before
    fun setup() {
        mockApiClient = mockk(relaxed = true)
        apiService = ApiService(mockApiClient)
        mockData = MockData(Mapper())
    }

    @Test
    fun `listCharacters should return Success with characters response`() = runTest {
        val request = ListCharacterRequest(filter = "test", page = 1)
        val expectedResponse = mockData.createListCharactersResponse()

        val mockResponse: Response<ListCharactersResponse> = mockk()

        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body() } returns expectedResponse
        coEvery { mockApiClient.listCharacters(request.filter, request.page) } returns mockResponse

        val result = apiService.listCharacters(request)
        assertTrue(result is Resource.Success)
        assertEquals(expectedResponse, (result as Resource.Success).data)
    }

    @Test
    fun `listCharacters should return Error with exception`() = runBlockingTest {
        val request = ListCharacterRequest(filter = "test", page = 1)
        val expectedException = Exception("error")
        coEvery { mockApiClient.listCharacters(request.filter, request.page) } throws expectedException

        val result = apiService.listCharacters(request)

        assertTrue(result is Resource.Error)
        assertEquals("error", (result as Resource.Error).error?.errorData?.error)
    }
}