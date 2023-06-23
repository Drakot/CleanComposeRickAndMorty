package com.aluengo.cleancomposerickandmorty.core.ui.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MviSingleEvent
interface MviViewState
interface MviIntent

interface MviViewModel<I : MviIntent, S : MviViewState, E : MviSingleEvent> {
    val viewState: StateFlow<S>
    val singleEvent: Flow<E>
    fun submitIntent(intent: I)
}
