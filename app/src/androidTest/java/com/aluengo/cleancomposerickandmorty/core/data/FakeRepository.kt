package com.aluengo.cleancomposerickandmorty.core.data

import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.domain.Repository
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharacterRequest
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeRepository @Inject constructor(
    private val mockData: MockData
) : Repository {
    override suspend fun listCharacters(request: ListCharacterRequest): Flow<Resource<ListCharactersDomain>> = flow {
        val apiResult = Resource.Success(mockData.createListCharactersDomain())
        emit(apiResult)
    }

    override fun getCharacter(id: Int): Flow<Resource<ListCharactersDomain.Result>> = flow {
        val apiResult = Resource.Success(mockData.createListCharactersDomain().results.firstOrNull())
        emit(apiResult)
    }

}