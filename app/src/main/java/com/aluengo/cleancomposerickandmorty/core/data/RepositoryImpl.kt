package com.aluengo.cleancomposerickandmorty.core.data

import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.data.local.LocalStorage
import com.aluengo.cleancomposerickandmorty.core.data.remote.ApiService
import com.aluengo.cleancomposerickandmorty.core.domain.Repository
import com.aluengo.cleancomposerickandmorty.core.utils.whenNotNull
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharacterRequest
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: ApiService, private val db: LocalStorage,
    private val mapper: Mapper
) : Repository {
    override suspend fun listCharacters(request: ListCharacterRequest): Flow<Resource<ListCharactersDomain>> = flow {
        api.listCharacters(request).also {
            if (it is Resource.Success) {
                db.saveCharacters(mapper.toLocalCharacters(it.data))
                db.saveCharactersPagination(mapper.toLocalCharactersPagination(it.data))
            } else {
                emit(Resource.Error(it.error))
            }

            val characters = mapper.toDomainCharacters(db.getCharacters(request.filter).first())
            val info = mapper.toDomainCharactersInfo(db.getInfo().first())

            whenNotNull(info, characters) { a, b ->
                emit(Resource.Success(ListCharactersDomain(a, b)))
            }
        }

    }

}