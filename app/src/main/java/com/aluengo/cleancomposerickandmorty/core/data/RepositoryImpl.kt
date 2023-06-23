package com.aluengo.cleancomposerickandmorty.core.data

import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.domain.Repository
import com.aluengo.cleancomposerickandmorty.listCharacters.data.ListCharactersResponse
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val api: ApiService) : Repository {
    override suspend fun listCharacters(): Resource<ListCharactersResponse> {
        return api.listCharacters()
    }

}