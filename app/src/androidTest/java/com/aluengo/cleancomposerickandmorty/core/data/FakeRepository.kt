package com.aluengo.cleancomposerickandmorty.core.data

import android.util.Log
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

        val apiResult = mockData.createListCharactersDomain()
            .copy(results = mockData.createListCharactersDomain().results.filter {
                it.name.contains(
                    request.filter.toString(),
                    true
                )
            })
        Log.e("FakeRepository", "filter: ${request.filter.toString()} count: ${apiResult.results.size}")
        val response = Resource.Success(apiResult)
        emit(response)
    }

    override fun getCharacter(id: Int): Flow<Resource<ListCharactersDomain.Result>> = flow {
        val apiResult = Resource.Success(mockData.createListCharactersDomain().results.firstOrNull())
        emit(apiResult)
    }

}