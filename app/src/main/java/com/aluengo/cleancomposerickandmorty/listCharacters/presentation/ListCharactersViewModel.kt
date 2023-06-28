package com.aluengo.cleancomposerickandmorty.listCharacters.presentation

import androidx.lifecycle.viewModelScope
import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.navigation.CharacterItemInput
import com.aluengo.cleancomposerickandmorty.core.navigation.NavRoutes
import com.aluengo.cleancomposerickandmorty.core.ui.SearchWidgetState
import com.aluengo.cleancomposerickandmorty.core.ui.mvi.AbstractMviViewModel
import com.aluengo.cleancomposerickandmorty.core.utils.logd
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharacterRequest
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListCharactersUseCase
import com.aluengo.cleancomposerickandmorty.listCharacters.domain.ListFilteredCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ListCharactersViewModel @Inject constructor(
    val listCharactersUseCase: ListCharactersUseCase,
    val listFilteredCharactersUseCase: ListFilteredCharactersUseCase
) : AbstractMviViewModel<ListCharactersIntent, ListCharactersState, ListCharactersUiSingleEvent>() {
    var pageInfo: ListCharactersUI.PageInfo? = null
    val itemsPerPage = 20
    override fun initState(): ListCharactersState = ListCharactersState()

    override fun submitIntent(intent: ListCharactersIntent) {
        when (intent) {
            is ListCharactersIntent.Load -> {
                pageInfo?.currentPage = 1
                callGetCharacters()
            }

            is ListCharactersIntent.EndOfListReached -> {
                pageInfo?.let {
                    logd("EndOfListReached")
                    if (!it.lastPage && !viewState.value.isLoading && viewState.value.data.isNotEmpty() &&
                        viewState.value.data.size >= itemsPerPage
                    ) {
                        pageInfo?.currentPage = it.currentPage + 1
                        callGetCharacters()
                    }
                }
            }

            is ListCharactersIntent.OnCharacterSelected -> {
                submitSingleEvent(
                    ListCharactersUiSingleEvent.OpenCharacter(
                        NavRoutes.Character.routeForCharacter(
                            CharacterItemInput(intent.item.id, intent.item.name)
                        )
                    )
                )
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

    private fun callGetCharacters() {
        val request = ListCharacterRequest(viewState.value.searchText, pageInfo?.currentPage ?: 1)

        submitState(viewState.value.copy(isLoading = true))

        if (viewState.value.searchText.isEmpty()) {
            getCharacters(request)
        } else {
            getCharactersWithFilter(request)
        }
    }

    private fun getCharacters(request: ListCharacterRequest) {
        viewModelScope.launch {
            listCharactersUseCase(request).collect {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let { listCharacterDomain ->
                            pageInfo = ListCharactersUI.infoFromDomain(listCharacterDomain.info)
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

    private fun getCharactersWithFilter(request: ListCharacterRequest) {
        viewModelScope.launch {
            listFilteredCharactersUseCase(request).collect {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let { listCharacterDomain ->
                            val isPaginating = (pageInfo?.currentPage ?: 0) > 1
                            pageInfo = ListCharactersUI.infoFromDomain(listCharacterDomain.info)
                            val result = ListCharactersUI.fromDomain(listCharacterDomain).results

                            val data = if (isPaginating) {
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