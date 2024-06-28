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

package com.leinardi.forlago.feature.account.ui.debug

import androidx.lifecycle.viewModelScope
import com.leinardi.forlago.feature.account.api.interactor.account.GetAccountInteractor
import com.leinardi.forlago.feature.account.api.interactor.account.RemoveAccountsInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.GetAccessTokenExpiryInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.GetAccessTokenInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.GetRefreshTokenInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.InvalidateAccessTokenInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.InvalidateRefreshTokenInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.PeekAccessTokenInteractor
import com.leinardi.forlago.feature.account.ui.debug.AccountDebugContract.Effect
import com.leinardi.forlago.feature.account.ui.debug.AccountDebugContract.Event
import com.leinardi.forlago.feature.account.ui.debug.AccountDebugContract.State
import com.leinardi.forlago.feature.login.api.destination.LogInDestination
import com.leinardi.forlago.library.navigation.api.navigator.ForlagoNavigator
import com.leinardi.forlago.library.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AccountDebugViewModel @Inject constructor(
    private val forlagoNavigator: ForlagoNavigator,
    private val getAccessTokenExpiryInteractor: GetAccessTokenExpiryInteractor,
    private val getAccessTokenInteractor: GetAccessTokenInteractor,
    private val getAccountInteractor: GetAccountInteractor,
    private val getRefreshTokenInteractor: GetRefreshTokenInteractor,
    private val invalidateAccessTokenInteractor: InvalidateAccessTokenInteractor,
    private val invalidateRefreshTokenInteractor: InvalidateRefreshTokenInteractor,
    private val peekAccessTokenInteractor: PeekAccessTokenInteractor,
    private val removeAccountsInteractor: RemoveAccountsInteractor,
) : BaseViewModel<Event, State, Effect>() {
    override fun provideInitialState() = State(null, null, null, null)

    override fun handleEvent(event: Event) {
        viewModelScope.launch {
            when (event) {
                Event.OnActivityPaused -> Timber.d(">>> Detached")
                Event.OnActivityResumed -> updateState()
                Event.OnGetAccessTokenClicked -> getAccessToken()
                Event.OnInvalidateAccessTokenClicked -> invalidateAccessToken()
                Event.OnInvalidateRefreshTokenClicked -> invalidateRefreshToken()
                Event.OnLogOutClicked -> logOut()
                Event.OnOpenLogInScreenClicked -> forlagoNavigator.navigate(LogInDestination.get())
            }
        }
    }

    private suspend fun invalidateAccessToken() {
        invalidateAccessTokenInteractor()
        updateState()
    }

    private suspend fun invalidateRefreshToken() {
        invalidateRefreshTokenInteractor()
        updateState()
    }

    private suspend fun getAccessToken() {
        val result = getAccessTokenInteractor()
        Timber.d("getAccessToken result = $result")
        updateState()
        if (result.isOk) {
            sendEffect { Effect.ShowSnackbar("Access token = ${result.value}") }
        } else {
            when (val accessTokenResult: GetAccessTokenInteractor.ErrResult = result.error) {
                is GetAccessTokenInteractor.ErrResult.AccountAuthenticatorError ->
                    sendEffect { Effect.ShowSnackbar(checkNotNull(accessTokenResult.errorMessage)) }

                is GetAccessTokenInteractor.ErrResult.AccountNotFound ->
                    sendEffect { Effect.ShowSnackbar("Account not found") }

                is GetAccessTokenInteractor.ErrResult.BadArgumentsError ->
                    sendEffect { Effect.ShowSnackbar(checkNotNull(accessTokenResult.errorMessage)) }

                is GetAccessTokenInteractor.ErrResult.NetworkError ->
                    sendEffect { Effect.ShowSnackbar(checkNotNull(accessTokenResult.errorMessage)) }

                is GetAccessTokenInteractor.ErrResult.ReAuthenticationRequired ->
                    sendEffect { Effect.ShowSnackbar("Re-Authentication required") }
            }
        }
    }

    private suspend fun logOut() {
        val message = if (removeAccountsInteractor()) {
            "Account removed"
        } else {
            "Unable to remove account!"
        }
        sendEffect { Effect.ShowSnackbar(message) }
        updateState()
    }

    private suspend fun updateState() {
        val account = getAccountInteractor()
        val refreshToken = getRefreshTokenInteractor()
        val accessToken = peekAccessTokenInteractor()
        val accessTokenExpiration = getAccessTokenExpiryInteractor()
        updateState { State(account?.name, refreshToken, accessToken, accessTokenExpiration) }
    }
}
