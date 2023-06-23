package com.aluengo.cleancomposerickandmorty.core.data

import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.listCharacters.data.ListCharactersResponse
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharacterRequest
import javax.inject.Inject

class ApiService @Inject constructor(private val client: ApiClient) : BaseService() {
    suspend fun listCharacters(request: ListCharacterRequest): Resource<ListCharactersResponse> {
        return apiCall { client.listCharacters(request.filter, request.page) }
    }
}