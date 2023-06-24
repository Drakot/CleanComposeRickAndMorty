package com.aluengo.cleancomposerickandmorty.core.data.network

import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.data.remote.BaseService
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.net.UnknownHostException

class BaseServiceTest {

    private lateinit var service: BaseService
    private val mockResponse: Response<String> = mockk()
    private val mockErrorBody: ResponseBody = mockk()

    @Before
    fun setup() {
        service = object : BaseService() {}
    }

    @Test
    fun `apiCall should return Success when response is successful`() = runBlocking {
        coEvery { mockResponse.isSuccessful } returns true
        every { mockResponse.body() } returns "Success"

        val result = service.apiCall { mockResponse }

        assertTrue(result is Resource.Success)
        assertEquals("Success", (result as Resource.Success).data)
    }

    @Test
    fun `apiCall should return Error when response is not successful`() = runBlocking {
        coEvery { mockResponse.isSuccessful } returns false
        every { mockResponse.code() } returns 400
        every { mockResponse.errorBody() } returns mockErrorBody
        every { mockErrorBody.string() } returns "{'error':'error'}"

        val result = service.apiCall { mockResponse }

        assertTrue(result is Resource.Error)
        assertEquals("error", (result as Resource.Error).error?.errorData?.error)
    }

    @Test
    fun `apiCall should return Error when response body is null`() = runBlocking {
        coEvery { mockResponse.isSuccessful } returns true
        every { mockResponse.body() } returns null
        every { mockResponse.code() } returns 400
        every { mockResponse.errorBody() } returns mockErrorBody
        every { mockErrorBody.string() } returns "{'error':'error'}"

        val result = service.apiCall { mockResponse }

        assertTrue(result is Resource.Error)
        assertEquals("error", (result as Resource.Error).error?.errorData?.error)
    }

    @Test
    fun `apiCall should return Error when an exception is thrown`() = runBlocking {
        val exception = UnknownHostException("No internet connection")
        coEvery { mockResponse.isSuccessful } throws exception

        val result = service.apiCall { mockResponse }

        assertTrue(result is Resource.Error)
        assertEquals("No internet connection", (result as Resource.Error).error?.errorData?.error)
    }

}