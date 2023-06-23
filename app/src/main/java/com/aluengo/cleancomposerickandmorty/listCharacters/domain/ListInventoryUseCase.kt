package com.aluengo.cleancomposerickandmorty.listCharacters.domain

import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.domain.Repository
import com.aluengo.cleancomposerickandmorty.listCharacters.data.ListCharactersResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ListCharactersUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(): Flow<Resource<ListCharactersResponse>> = flow {
        emit(repository.listCharacters())
    }
}