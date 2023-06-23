package com.aluengo.cleancomposerickandmorty.core.data

import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.listCharacters.data.ListCharactersResponse
import javax.inject.Inject

class ApiService @Inject constructor(private val client: ApiClient) : BaseService() {
    suspend fun listCharacters(): Resource<ListCharactersResponse> {
        return apiCall { client.listCharacters() }
    }
}