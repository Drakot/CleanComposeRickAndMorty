package com.aluengo.cleancomposerickandmorty.core.data.remote

import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.data.ErrorData
import com.aluengo.cleancomposerickandmorty.core.data.ErrorResponse
import com.aluengo.cleancomposerickandmorty.core.data.ErrorType
import com.aluengo.cleancomposerickandmorty.core.utils.loge
import com.aluengo.cleancomposerickandmorty.core.utils.logw
import com.google.gson.Gson
import retrofit2.Response
import java.net.UnknownHostException

abstract class BaseService {

    suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): Resource<T> {
        val response: Response<T>
        try {
            response = call.invoke()

            return if (!response.isSuccessful) {
                val errorResponse = mapErrorResponse(response)
                logw(errorResponse.errorData?.error)
                Resource.Error(errorResponse)
            } else {
                if (response.body() == null) {
                    Resource.Error(mapErrorResponse(response))
                } else {
                    Resource.Success(response.body()!!)
                }
            }
        } catch (t: Throwable) {
            loge(t.message)
            return Resource.Error(mapErrorResponse(t))
        }
    }

    private fun <T> mapErrorResponse(response: Response<T>): ErrorResponse {
        val errorBody = response.errorBody()?.string()
        val errorData = try {
            val parsedData = Gson().fromJson(errorBody, ErrorData::class.java)

            parsedData
        } catch (e: java.lang.Exception) {
            loge(e.message.toString())
            null
        }

        return ErrorResponse(response.code(), errorData)
    }

    private fun mapErrorResponse(t: Throwable): ErrorResponse {
        val type = when (t) {
            is UnknownHostException -> {
                ErrorType.NotConnected
            }

            else -> {
                ErrorType.Default
            }
        }

        return ErrorResponse(0, ErrorData(t.message, type))
    }
}