package com.aluengo.cleancomposerickandmorty.core.domain

import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharacterRequest
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersDomain
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun listCharacters(request: ListCharacterRequest): Flow<Resource<ListCharactersDomain>>
    fun getCharacter(id: Int): Flow<Resource<ListCharactersDomain.Result>>
    fun listCharactersWithFilter(request: ListCharacterRequest): Flow<Resource<ListCharactersDomain>>
}