package com.aluengo.cleancomposerickandmorty.core.data.remote

import com.aluengo.cleancomposerickandmorty.listCharacters.data.ListCharactersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {
    @GET("character")
    suspend fun listCharacters(
        @Query("name") filter: String?,
        @Query("page") page: Int
    ): Response<ListCharactersResponse>

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): Response<ListCharactersResponse.Result>
}