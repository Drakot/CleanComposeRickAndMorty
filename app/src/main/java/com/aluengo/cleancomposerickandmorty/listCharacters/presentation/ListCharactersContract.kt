package com.aluengo.cleancomposerickandmorty.listCharacters.presentation

import com.aluengo.cleancomposerickandmorty.core.data.ErrorType
import com.aluengo.cleancomposerickandmorty.core.ui.mvi.MviIntent
import com.aluengo.cleancomposerickandmorty.core.ui.mvi.MviSingleEvent
import com.aluengo.cleancomposerickandmorty.core.ui.mvi.MviViewState
import com.aluengo.cleancomposerickandmorty.detailCharacter.presentation.SearchWidgetState

sealed class ListCharactersIntent : MviIntent {
    object Load : ListCharactersIntent()
    object OnSearchClicked : ListCharactersIntent()
    object OnCloseSearchClick : ListCharactersIntent()
    data class OnTypeSearch(val text: String) : ListCharactersIntent()
    data class OnSearch(val filter: String) : ListCharactersIntent()
    data class OnCharacterSelected(val item: ListCharactersUI.Result) : ListCharactersIntent()
    object EndOfListReached : ListCharactersIntent()
}

data class ListCharactersState(
    val isLoading: Boolean = false,
    val searchState: SearchWidgetState = SearchWidgetState.CLOSED,
    val searchText: String = "",
    val currentPage: Int = 1,
    val data: List<ListCharactersUI.Result> = emptyList()
) : MviViewState

sealed class ListCharactersUiSingleEvent : MviSingleEvent {
    data class ShowMessage(val message: String) : ListCharactersUiSingleEvent()
    data class ShowError(val errorType: ErrorType?) : ListCharactersUiSingleEvent()
}