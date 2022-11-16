/*
 * Copyright 2022 Roberto Leinardi.
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

package com.leinardi.forlago.feature.account.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.leinardi.forlago.feature.account.ui.SignInContract.Effect
import com.leinardi.forlago.feature.account.ui.SignInContract.Event
import com.leinardi.forlago.feature.account.ui.SignInContract.State
import com.leinardi.forlago.library.android.interactor.account.AddAccountInteractor
import com.leinardi.forlago.library.android.interactor.account.GetAccessTokenInteractor
import com.leinardi.forlago.library.android.interactor.account.GetAccountInteractor
import com.leinardi.forlago.library.android.interactor.account.SetRefreshTokenInteractor
import com.leinardi.forlago.library.navigation.api.destination.account.SignInDestination
import com.leinardi.forlago.library.navigation.api.navigator.ForlagoNavigator
import com.leinardi.forlago.library.network.interactor.account.SignInInteractor
import com.leinardi.forlago.library.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val addAccountInteractor: AddAccountInteractor,
    private val getAccountInteractor: GetAccountInteractor,
    private val getAccessTokenInteractor: GetAccessTokenInteractor,
    private val setRefreshTokenInteractor: SetRefreshTokenInteractor,
    private val savedStateHandle: SavedStateHandle,
    private val signInInteractor: SignInInteractor,
    private val forlagoNavigator: ForlagoNavigator,
) : BaseViewModel<Event, State, Effect>() {
    override fun provideInitialState(): State {
        val username = getAccountInteractor()?.name
        val reauthenticate: Boolean = SignInDestination.Arguments.getReauthenticate(savedStateHandle)
        return State(
            isReauthenticate = reauthenticate && username != null,
            username = username.orEmpty(),
            password = "",
        )
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OnSignInButtonClicked -> signIn(event.username, event.password)
        }
    }

    private fun signIn(username: String, password: String) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            when (val result = signInInteractor(username, password)) {
                is SignInInteractor.Result.Success -> handleSuccessfulSignIn(result.refreshToken, username)
                is SignInInteractor.Result.Failure.BadAuthentication -> sendEffect {
                    Effect.ShowErrorSnackbar(
                        "SignInInteractor.Result.Failure.BadAuthentication",
                        "OK",
                    )
                }
                is SignInInteractor.Result.Failure.NetworkError -> sendEffect {
                    Effect.ShowErrorSnackbar(
                        "SignInInteractor.Result.Failure.NetworkError",
                        "OK",
                    )
                }
                is SignInInteractor.Result.Failure.UnexpectedError -> sendEffect {
                    Effect.ShowErrorSnackbar(
                        "SignInInteractor.Result.Failure.UnexpectedError",
                        "OK",
                    )
                }
            }
            updateState { copy(isLoading = false) }
        }
    }

    private suspend fun handleSuccessfulSignIn(
        refreshToken: String,
        username: String,
    ) {
        if (viewState.value.isReauthenticate) {
            if (setRefreshTokenInteractor(refreshToken)) {
                getAccessTokenInteractor()
                forlagoNavigator.navigateBack()
            }
        } else {
            addAccountInteractor(username, refreshToken)
            forlagoNavigator.navigateHome()
        }
    }
}
