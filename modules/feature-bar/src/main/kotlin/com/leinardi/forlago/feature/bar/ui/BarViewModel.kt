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

package com.leinardi.forlago.feature.bar.ui

import androidx.lifecycle.SavedStateHandle
import com.leinardi.forlago.feature.bar.api.destination.BarDestination
import com.leinardi.forlago.feature.bar.ui.BarContract.Effect
import com.leinardi.forlago.feature.bar.ui.BarContract.Event
import com.leinardi.forlago.feature.bar.ui.BarContract.State
import com.leinardi.forlago.library.navigation.api.navigator.ForlagoNavigator
import com.leinardi.forlago.library.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BarViewModel @Inject constructor(
    private val forlagoNavigator: ForlagoNavigator,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<Event, State, Effect>() {
    val id: String? = BarDestination.Arguments.getId(savedStateHandle)
    override fun provideInitialState() = State(editModeEnabled = id == null, text = BarDestination.Arguments.getText(savedStateHandle).orEmpty())

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OnBackButtonClicked -> forlagoNavigator.navigateBack()
            is Event.OnUpButtonClicked -> forlagoNavigator.navigateUp()
        }
    }
}
