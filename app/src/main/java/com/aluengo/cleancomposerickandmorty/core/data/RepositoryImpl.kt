package com.aluengo.cleancomposerickandmorty.core.data

import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.domain.Repository
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val api: ApiService) : Repository {
    override suspend fun listCharacters(): Flow<Resource<ListCharactersDomain>> = flow {
        api.listCharacters().also {
            if (it is Resource.Success) {
                emit(Resource.Success(it.data?.toDomain()))
            } else {
                emit(Resource.Error(it.error))
            }
        }
    }

}