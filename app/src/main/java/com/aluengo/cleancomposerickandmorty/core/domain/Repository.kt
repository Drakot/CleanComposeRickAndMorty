package com.aluengo.cleancomposerickandmorty.core.domain

import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.listCharacters.data.ListCharactersResponse

interface Repository {
    suspend fun listCharacters(): Resource<ListCharactersResponse>
}