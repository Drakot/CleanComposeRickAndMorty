package com.aluengo.cleancomposerickandmorty.listCharacters.presentation

import androidx.lifecycle.viewModelScope
import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.ui.mvi.AbstractMviViewModel
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
open class ListCharactersViewModel @Inject constructor(
    val listCharactersUseCase: ListCharactersUseCase
) : AbstractMviViewModel<ListCharactersIntent, ListCharactersState, ListCharactersUiSingleEvent>() {

    override fun initState(): ListCharactersState = ListCharactersState()

    override fun submitIntent(intent: ListCharactersIntent) {
        when (intent) {
            is ListCharactersIntent.Load -> {
                callGetCharacters()
            }
            is ListCharactersIntent.EndOfListReached -> {
                Timber.tag("ListCharactersViewModel").d("EndOfListReached")
            }

            is ListCharactersIntent.OnCharacterSelected -> {
                Timber.tag("ListCharactersViewModel").d("OnCharacterSelected")
            }
            is ListCharactersIntent.OnCloseSearchClick -> {
                Timber.tag("ListCharactersViewModel").d("OnCloseSearchClick")
            }
            is ListCharactersIntent.OnSearch -> {
                Timber.tag("ListCharactersViewModel").d("OnSearch")
            }
            is ListCharactersIntent.OnSearchClicked -> {
                Timber.tag("ListCharactersViewModel").d("OnSearchClicked")
            }
            is ListCharactersIntent.OnTypeSearch -> {
                Timber.tag("ListCharactersViewModel").d("OnTypeSearch")
            }
        }
    }

    private fun callGetCharacters() {

        submitState(viewState.value.copy(isLoading = true))

        viewModelScope.launch {
            listCharactersUseCase().collect {
                when (it) {
                    is Resource.Success -> {
                        Timber.tag("ListCharactersViewModel").d("callGetCharacters : ${it.data?.results}")
                        it.data?.let {listCharacterDomain ->
                            submitState(viewState.value.copy(data = ListCharactersUI.fromDomain(listCharacterDomain).results))
                        }
                    }

                    is Resource.Error -> {
                        submitSingleEvent(ListCharactersUiSingleEvent.ShowError(it.error?.errorType))
                    }
                }
                submitState(viewState.value.copy(isLoading = false))
            }
        }
    }
}