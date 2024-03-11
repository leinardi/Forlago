/*
 * Copyright 2024 Roberto Leinardi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leinardi.forlago.library.ui.base

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.debounce
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger

abstract class BaseViewModel<UiEvent : ViewEvent, UiState : ViewState, UiEffect : ViewEffect> : ViewModel() {
    // State (current state of views)
    // Everything is lazy in order to be able to use SavedStateHandle as initial value
    private val initialState: UiState by lazy { provideInitialState() }
    private val _viewState: MutableState<UiState> by lazy { mutableStateOf(initialState) }
    val viewState: State<UiState> by lazy { _viewState }

    private val runningJobs = AtomicInteger(0)
    private val debouncedErrorChannel = Channel<suspend () -> Unit>()

    // Event (user actions)
    private val _event: MutableSharedFlow<UiEvent> = MutableSharedFlow()

    // Effect (side effects like error messages which we want to show only once)
    private val _effect: Channel<UiEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        _event.onEach {
            handleEvent(it)
        }.launchIn(viewModelScope)

        debouncedErrorChannel
            .consumeAsFlow()
            .debounce(Duration.ofSeconds(1))
            .onEach { it() }
            .launchIn(viewModelScope)
    }

    abstract fun provideInitialState(): UiState

    protected suspend fun debounceError(error: suspend () -> Unit) {
        debouncedErrorChannel.send(error)
    }

    open fun onLoadingChanged(loading: Boolean) {}

    protected fun updateState(reducer: UiState.() -> UiState) {
        val newState = viewState.value.reducer()
        _viewState.value = newState
    }

    fun onUiEvent(event: UiEvent) {
        viewModelScope.launch { _event.emit(event) }
    }

    abstract fun handleEvent(event: UiEvent)

    protected fun sendEffect(effectBuilder: () -> UiEffect) {
        viewModelScope.launch { _effect.send(effectBuilder()) }
    }

    protected suspend fun load(block: suspend () -> Unit) {
        require(runningJobs.get() >= 0)
        if (runningJobs.get() == 0) {
            onLoadingChanged(true)
        }
        runningJobs.incrementAndGet()
        block()
        if (runningJobs.decrementAndGet() == 0) {
            onLoadingChanged(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        _effect.close()
        debouncedErrorChannel.close()
    }
}

interface ViewState

interface ViewEvent

interface ViewEffect
