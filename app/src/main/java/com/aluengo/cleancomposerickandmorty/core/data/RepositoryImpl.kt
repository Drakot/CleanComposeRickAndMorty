package com.aluengo.cleancomposerickandmorty.core.data

import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.domain.Repository
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharacterRequest
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val api: ApiService) : Repository {
    override suspend fun listCharacters(request: ListCharacterRequest): Flow<Resource<ListCharactersDomain>> = flow {
        val data = api.listCharacters(request)

        if (data is Resource.Success) {
            emit(Resource.Success(data.data?.toDomain()))
        } else {
            emit(Resource.Error(data.error))
        }

    }

}