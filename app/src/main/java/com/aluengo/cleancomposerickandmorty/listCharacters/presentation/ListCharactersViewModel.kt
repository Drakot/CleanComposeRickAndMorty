package com.aluengo.cleancomposerickandmorty.listCharacters.presentation

import com.aluengo.cleancomposerickandmorty.core.ui.mvi.AbstractMviViewModel
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class ListCharactersViewModel @Inject constructor(
    val listCharactersUseCase: ListCharactersUseCase
) : AbstractMviViewModel<ListCharactersIntent, ListCharactersState, ListCharactersUiSingleEvent>() {

    override fun initState(): ListCharactersState = ListCharactersState()

    override fun submitIntent(intent: ListCharactersIntent) {
        when (intent) {
            is ListCharactersIntent.EndOfListReached -> {}
            is ListCharactersIntent.Load -> {}
            is ListCharactersIntent.OnCharacterSelected -> {}
            is ListCharactersIntent.OnCloseSearchClick -> {}
            is ListCharactersIntent.OnSearch -> {}
            is ListCharactersIntent.OnSearchClicked -> {}
            is ListCharactersIntent.OnTypeSearch -> {}
        }
    }
}