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

package com.leinardi.template.account.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.leinardi.template.account.interactor.AddAccountInteractor
import com.leinardi.template.account.interactor.GetAccessTokenInteractor
import com.leinardi.template.account.interactor.GetAccountInteractor
import com.leinardi.template.account.interactor.SetRefreshTokenInteractor
import com.leinardi.template.account.interactor.SignInInteractor
import com.leinardi.template.account.ui.AccountAuthenticatorContract.Effect
import com.leinardi.template.account.ui.AccountAuthenticatorContract.Event
import com.leinardi.template.account.ui.AccountAuthenticatorContract.State
import com.leinardi.template.navigation.TemplateNavigator
import com.leinardi.template.navigation.destination.account.AccountAuthenticatorDestination
import com.leinardi.template.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AccountAuthenticatorViewModel @Inject constructor(
    private val addAccountInteractor: AddAccountInteractor,
    private val getAccountInteractor: GetAccountInteractor,
    private val getAccessTokenInteractor: GetAccessTokenInteractor,
    private val setRefreshTokenInteractor: SetRefreshTokenInteractor,
    private val savedStateHandle: SavedStateHandle,
    private val signInInteractor: SignInInteractor,
    private val templateNavigator: TemplateNavigator,
) : BaseViewModel<Event, State, Effect>() {
    override fun provideInitialState(): State {
        val account = getAccountInteractor()
        return State(savedStateHandle[AccountAuthenticatorDestination.RELOGIN_PARAM] ?: false, account?.name.orEmpty(), "")
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OnSignInButtonClicked -> signIn(event.username, event.password)
        }
    }

    private fun signIn(username: String, password: String) {
        viewModelScope.launch {
            updateState { viewState.value.copy(isLoading = true) }
            when (val result = signInInteractor(username, password)) {
                is SignInInteractor.Result.Success -> handleSuccessfulSignIn(result.refreshToken, username)
                SignInInteractor.Result.Failure.BadAuthentication -> Timber.d("> SignInInteractor.Result.Failure.BadAuthentication")  // TODO
                SignInInteractor.Result.Failure.NetworkError -> Timber.d("> SignInInteractor.Result.Failure.NetworkError")  // TODO
            }
            updateState { viewState.value.copy(isLoading = false) }
        }
    }

    private suspend fun handleSuccessfulSignIn(
        refreshToken: String,
        username: String
    ) {
        if (viewState.value.isRelogin) {
            if (setRefreshTokenInteractor(refreshToken)) {
                getAccessTokenInteractor()
            }
        } else {
            addAccountInteractor(username, refreshToken)
        }
        templateNavigator.navigateBack()
    }
}
