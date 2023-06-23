package com.aluengo.cleancomposerickandmorty.listCharacters.presentation

import androidx.lifecycle.viewModelScope
import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.ui.SearchWidgetState
import com.aluengo.cleancomposerickandmorty.core.ui.mvi.AbstractMviViewModel
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharacterRequest
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
open class ListCharactersViewModel @Inject constructor(
    val listCharactersUseCase: ListCharactersUseCase
) : AbstractMviViewModel<ListCharactersIntent, ListCharactersState, ListCharactersUiSingleEvent>() {
    var pageInfo: ListCharactersUI.PageInfo? = null

    override fun initState(): ListCharactersState = ListCharactersState()

    override fun submitIntent(intent: ListCharactersIntent) {
        when (intent) {
            is ListCharactersIntent.Load -> {
                pageInfo?.currentPage = 1
                callGetCharacters()
            }

            is ListCharactersIntent.EndOfListReached -> {
                pageInfo?.let {
                    if (!it.lastPage && !viewState.value.isLoading && viewState.value.data.isNotEmpty()) {
                        Timber.tag("ListCharactersViewModel").d("EndOfListReached")
                        pageInfo?.currentPage = it.currentPage + 1
                        callGetCharacters(true)
                    }
                }
            }

            is ListCharactersIntent.OnCharacterSelected -> {

            }

            is ListCharactersIntent.OnCloseSearchClick -> {
                submitState(viewState.value.copy(searchState = SearchWidgetState.CLOSED))
                callGetCharacters()
            }

            is ListCharactersIntent.OnSearch -> {
                pageInfo?.currentPage = 1
                callGetCharacters()
            }

            is ListCharactersIntent.OnSearchClicked -> {
                submitState(viewState.value.copy(searchState = SearchWidgetState.OPENED))
            }

            is ListCharactersIntent.OnTypeSearch -> {
                submitState(viewState.value.copy(searchText = intent.text))
            }
        }
    }

    private fun callGetCharacters(add: Boolean = false) {
        val request = ListCharacterRequest(viewState.value.searchText, pageInfo?.currentPage ?: 1)

        submitState(viewState.value.copy(isLoading = true))

        viewModelScope.launch {
            listCharactersUseCase(request).collect {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let { listCharacterDomain ->
                            pageInfo = ListCharactersUI.infoFromDomain(listCharacterDomain.info)
                            val result = ListCharactersUI.fromDomain(listCharacterDomain).results
                            val data = if (add) {
                                viewState.value.data + result
                            } else {
                                result
                            }
                            submitState(viewState.value.copy(data = data))
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