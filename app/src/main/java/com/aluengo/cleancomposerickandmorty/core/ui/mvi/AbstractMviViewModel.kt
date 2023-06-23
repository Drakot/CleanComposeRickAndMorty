package com.aluengo.cleancomposerickandmorty.core.ui.mvi

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber


abstract class AbstractMviViewModel<I : MviIntent, S : MviViewState, E : MviSingleEvent> :
    MviViewModel<I, S, E>, ViewModel() {
    protected open val rawLogTag: String? = null

    private val _viewState: MutableStateFlow<S> by lazy {
        MutableStateFlow(initState())
    }
    override val viewState: StateFlow<S> = _viewState

    abstract override fun submitIntent(intent: I)
    abstract fun initState(): S

    init {
        submitState(initState())
    }

    protected val logTag by lazy(LazyThreadSafetyMode.PUBLICATION) {
        (rawLogTag ?: this::class.java.simpleName).let { tag: String ->
            tag
        }
    }

    private val eventChannel = Channel<E>(Channel.UNLIMITED)
    override val singleEvent: Flow<E> = eventChannel.receiveAsFlow()


    @CallSuper
    override fun onCleared() {
        super.onCleared()
        eventChannel.close()
        Timber.tag(logTag).d("onCleared")
    }

    open fun submitSingleEvent(event: E) {
        eventChannel.trySend(event)
            .onSuccess { Timber.tag(logTag).d("sendEvent: event=$event") }
            .onFailure {
                Timber
                    .tag(logTag)
                    .e(it, "Failed to send event: $event")
            }
            .getOrThrow()
    }

    fun submitStateScope(state: S) {
        viewModelScope.launch {
            _viewState.value = state
        }
    }

    fun submitState(state: S) {
        _viewState.value = state
    }


}