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

package com.leinardi.forlago.feature.foo.ui.foo

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.leinardi.forlago.feature.foo.ui.foo.FooContract.Effect
import com.leinardi.forlago.feature.foo.ui.foo.FooContract.Event
import com.leinardi.forlago.feature.foo.ui.foo.FooContract.State
import com.leinardi.forlago.library.navigation.api.destination.bar.BarDestination
import com.leinardi.forlago.library.navigation.api.destination.foo.FooDialogDestination
import com.leinardi.forlago.library.navigation.api.navigator.ForlagoNavigator
import com.leinardi.forlago.library.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class FooViewModel @Inject constructor(
    private val forlagoNavigator: ForlagoNavigator,
    private val app: Application,
) : BaseViewModel<Event, State, Effect>() {
    override fun provideInitialState() = State(app.getString(com.leinardi.forlago.library.i18n.R.string.i18n_foo_change_me))

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OnBarButtonClicked -> sendText(event.text)
            Event.OnShowSnackbarButtonClicked -> sendEffect {
                Effect.ShowSnackbar(
                    app.getString(com.leinardi.forlago.library.i18n.R.string.i18n_foo_snackbar_text),
                )
            }

            Event.OnShowMoreFooButtonClicked -> forlagoNavigator.navigate(FooDialogDestination.get())
        }
    }

    private fun sendText(text: String) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            delay(TimeUnit.SECONDS.toMillis(2))
            updateState { copy(isLoading = false) }
            forlagoNavigator.navigate(BarDestination.get(text))
        }
    }
}
