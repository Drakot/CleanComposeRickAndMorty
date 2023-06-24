package com.aluengo.cleancomposerickandmorty.detailCharacter.domain

import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.domain.Repository
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharacterUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(id: Int): Flow<Resource<ListCharactersDomain.Result>> {
        return repository.getCharacter(id)
    }
}