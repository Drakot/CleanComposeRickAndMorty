package com.aluengo.cleancomposerickandmorty.listCharacters.domain

import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.domain.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListFilteredCharactersUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(request: ListCharacterRequest): Flow<Resource<ListCharactersDomain>> {
        return repository.listCharactersWithFilter(request)
    }
}