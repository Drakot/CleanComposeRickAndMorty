package com.aluengo.cleancomposerickandmorty.core.domain

import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersDomain
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun listCharacters(): Flow<Resource<ListCharactersDomain>>
}