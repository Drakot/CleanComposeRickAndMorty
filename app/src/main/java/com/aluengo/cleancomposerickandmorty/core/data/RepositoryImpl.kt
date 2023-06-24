package com.aluengo.cleancomposerickandmorty.core.data

import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.data.local.LocalStorage
import com.aluengo.cleancomposerickandmorty.core.data.remote.ApiService
import com.aluengo.cleancomposerickandmorty.core.domain.Repository
import com.aluengo.cleancomposerickandmorty.core.utils.whenNotNull
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharacterRequest
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: ApiService, private val db: LocalStorage,
    private val mapper: Mapper
) : Repository {
    override suspend fun listCharacters(request: ListCharacterRequest): Flow<Resource<ListCharactersDomain>> = flow {
        emitCacheResult(request)

        val apiResult = api.listCharacters(request)
        if (apiResult is Resource.Success) {

            db.saveCharacters(mapper.toLocalCharacters(apiResult.data))
            db.saveCharactersPagination(mapper.toLocalCharactersPagination(apiResult.data))

            emitCacheResult(request)
        } else {
            emit(Resource.Error(apiResult.error))
        }
    }

    private suspend fun FlowCollector<Resource<ListCharactersDomain>>.emitCacheResult(request: ListCharacterRequest) {
        val characters = mapper.toDomainCharacters(db.getCharacters(request.filter).firstOrNull())
        val info = mapper.toDomainCharactersInfo(db.getInfo().firstOrNull())

        whenNotNull(info, characters) { a, b ->
            emit(Resource.Success(ListCharactersDomain(a, b)))
        }
    }

    override fun getCharacter(id: Int): Flow<Resource<ListCharactersDomain.Result>> = flow {
        val character = mapper.toDomainCharacterFromLocal(db.getCharacter(id).firstOrNull())
        character?.let {
            emit(Resource.Success(character))
        }
        val apiResult = api.getCharacter(id)
        if (apiResult is Resource.Success) {
            mapper.toDomainCharacter(apiResult.data)?.let {
                db.saveCharacter(mapper.toLocalCharacter(apiResult.data))
                emit(Resource.Success(it))
            }
        } else {
            emit(Resource.Error(apiResult.error))
        }
    }

}