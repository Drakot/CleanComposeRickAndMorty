package com.aluengo.cleancomposerickandmorty.core.data

import com.aluengo.cleancomposerickandmorty.listCharacters.data.ListCharactersResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiClient {
    @GET("character")
    suspend fun listCharacters(): Response<ListCharactersResponse>
}