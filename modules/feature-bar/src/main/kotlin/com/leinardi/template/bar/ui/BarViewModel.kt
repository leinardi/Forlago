/*
 * Copyright 2021 Roberto Leinardi.
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

package com.leinardi.template.bar.ui

import androidx.lifecycle.SavedStateHandle
import com.leinardi.template.navigation.TemplateNavigator
import com.leinardi.template.navigation.destination.bar.BarDestination
import com.leinardi.template.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BarViewModel @Inject constructor(
    private val templateNavigator: TemplateNavigator,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<BarContract.Event, BarContract.State, BarContract.Effect>() {
    override fun provideInitialState() = BarContract.State(savedStateHandle[BarDestination.TEXT_PARAM] ?: "")

    override fun handleEvent(event: BarContract.Event) {
        when (event) {
            is BarContract.Event.OnBackButtonClicked,
            is BarContract.Event.OnUpButtonClicked -> templateNavigator.navigateUp()
        }
    }
}
