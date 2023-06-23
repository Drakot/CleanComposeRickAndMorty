package com.aluengo.cleancomposerickandmorty.core.data

import com.aluengo.cleancomposerickandmorty.core.Resource
import com.google.gson.Gson
import retrofit2.Response
import timber.log.Timber
import java.net.UnknownHostException

abstract class BaseService {

    suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): Resource<T> {
        val response: Response<T>
        try {
            response = call.invoke()

            return if (!response.isSuccessful) {
                val errorResponse = mapErrorResponse(response)
                Timber.tag("BaseService").d(errorResponse.errorData?.error)
                Resource.Error(errorResponse)
            } else {
                if (response.body() == null) {
                    Resource.Error(mapErrorResponse(response))
                } else {
                    Resource.Success(response.body()!!)
                }
            }
        } catch (t: Throwable) {
            Timber.tag("BaseService").e(t)
            return Resource.Error(mapErrorResponse(t))
        }
    }

    private fun <T> mapErrorResponse(response: Response<T>): ErrorResponse {
        val errorBody = response.errorBody()?.string()
        val errorData = try {
            val parsedData = Gson().fromJson(errorBody, ErrorData::class.java)

            parsedData
        } catch (e: java.lang.Exception) {
            Timber.tag("BaseService").e(e.message.toString())
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