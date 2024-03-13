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

package com.leinardi.forlago.feature.login.ui

import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.leinardi.forlago.feature.account.api.interactor.account.AddAccountInteractor
import com.leinardi.forlago.feature.account.api.interactor.account.GetAccountInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.GetAccessTokenInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.SetRefreshTokenInteractor
import com.leinardi.forlago.feature.login.api.interactor.LogInInteractor
import com.leinardi.forlago.feature.login.ui.LogInContract.Effect
import com.leinardi.forlago.feature.login.ui.LogInContract.Event
import com.leinardi.forlago.feature.login.ui.LogInContract.State
import com.leinardi.forlago.library.navigation.api.navigator.ForlagoNavigator
import com.leinardi.forlago.library.network.api.model.AuthErrResult
import com.leinardi.forlago.library.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val addAccountInteractor: AddAccountInteractor,
    private val forlagoNavigator: ForlagoNavigator,
    private val getAccessTokenInteractor: GetAccessTokenInteractor,
    private val getAccountInteractor: GetAccountInteractor,
    private val logInInteractor: LogInInteractor,
    private val setRefreshTokenInteractor: SetRefreshTokenInteractor,
) : BaseViewModel<Event, State, Effect>() {
    override fun provideInitialState(): State {
        val username = getAccountInteractor()?.name
        return State(
            isReauthenticate = username != null,
            username = username.orEmpty(),
            password = "",
        )
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OnLogInButtonClicked -> logIn(event.username, event.password)
        }
    }

    private fun logIn(username: String, password: String) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            when (val result = logInInteractor(username, password)) {
                is Ok -> handleSuccessfulLogIn(result.value.refreshToken, username)
                is Err -> when (result.error) {
                    is AuthErrResult.BadAuthentication -> sendEffect {
                        Effect.ShowErrorSnackbar(
                            "BadAuthentication",
                            "OK",
                        )
                    }

                    is AuthErrResult.NetworkError -> sendEffect {
                        Effect.ShowErrorSnackbar(
                            "NetworkError",
                            "OK",
                        )
                    }

                    is AuthErrResult.UnexpectedError -> sendEffect {
                        Effect.ShowErrorSnackbar(
                            "UnexpectedError",
                            "OK",
                        )
                    }
                }
            }
            updateState { copy(isLoading = false) }
        }
    }

    private suspend fun handleSuccessfulLogIn(
        refreshToken: String,
        username: String,
    ) {
        if (viewState.value.isReauthenticate) {
            if (setRefreshTokenInteractor(refreshToken)) {
                getAccessTokenInteractor()
                forlagoNavigator.navigateBackOrHome()
            }
        } else {
            addAccountInteractor(username, refreshToken)
            forlagoNavigator.navigateHome()
        }
    }
}
