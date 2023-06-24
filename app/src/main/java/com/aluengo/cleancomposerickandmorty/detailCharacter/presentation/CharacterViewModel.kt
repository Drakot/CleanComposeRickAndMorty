package com.aluengo.cleancomposerickandmorty.detailCharacter.presentation

import androidx.lifecycle.viewModelScope
import com.aluengo.cleancomposerickandmorty.core.Resource
import com.aluengo.cleancomposerickandmorty.core.ui.mvi.AbstractMviViewModel
import com.aluengo.cleancomposerickandmorty.detailCharacter.domain.GetCharacterUseCase
import com.aluengo.cleancomposerickandmorty.listCharacters.presentation.ListCharactersUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
open class CharacterViewModel @Inject constructor(
    val getCharacterUseCase: GetCharacterUseCase
) : AbstractMviViewModel<CharacterIntent, CharacterState, CharacterUiSingleEvent>() {

    override fun initState(): CharacterState = CharacterState()
    override fun submitIntent(intent: CharacterIntent) {
        when (intent) {
            is CharacterIntent.Load -> {
                intent.id?.let { callGetCharacter(it) }
            }

            is CharacterIntent.OnClickNavIcon -> {

            }
        }
    }

    private fun callGetCharacter(id: Int) {
        submitState(viewState.value.copy(isLoading = true))

        viewModelScope.launch {
            getCharacterUseCase(id).collect {
                when (it) {
                    is Resource.Success -> {
                        Timber.tag("ListCharactersViewModel").d("callGetCharacters : ${it.data}")

                        it.data?.let { listCharacterDomain ->
                            submitState(viewState.value.copy(data = ListCharactersUI.fromDomain(listCharacterDomain)))
                        }
                    }

                    is Resource.Error -> {
                        submitSingleEvent(CharacterUiSingleEvent.ShowError(it.error?.errorType))
                    }
                }

                submitState(viewState.value.copy(isLoading = false))
            }
        }
    }

}