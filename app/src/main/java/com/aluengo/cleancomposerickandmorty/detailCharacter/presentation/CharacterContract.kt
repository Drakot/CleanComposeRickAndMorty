package com.aluengo.cleancomposerickandmorty.detailCharacter.presentation

import com.aluengo.cleancomposerickandmorty.core.data.ErrorType
import com.aluengo.cleancomposerickandmorty.core.navigation.CharacterItemInput
import com.aluengo.cleancomposerickandmorty.core.ui.mvi.MviIntent
import com.aluengo.cleancomposerickandmorty.core.ui.mvi.MviSingleEvent
import com.aluengo.cleancomposerickandmorty.core.ui.mvi.MviViewState
import com.aluengo.cleancomposerickandmorty.listCharacters.presentation.ListCharactersUI

sealed class CharacterIntent : MviIntent {
    data class Load(val characterItemInput: CharacterItemInput) : CharacterIntent()
    object OnClickNavIcon : CharacterIntent()
}

data class CharacterState(
    val isLoading: Boolean = false,
    val data: ListCharactersUI.Result? = null
) : MviViewState

sealed class CharacterUiSingleEvent : MviSingleEvent {
    object GoBack : CharacterUiSingleEvent()
    data class ShowError(val errorType: ErrorType?) : CharacterUiSingleEvent()
}