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

package com.leinardi.forlago.feature.account.ui.debug

import androidx.lifecycle.viewModelScope
import com.leinardi.forlago.core.android.interactor.account.GetAccessTokenExpiryInteractor
import com.leinardi.forlago.core.android.interactor.account.GetAccessTokenInteractor
import com.leinardi.forlago.core.android.interactor.account.GetAccountInteractor
import com.leinardi.forlago.core.android.interactor.account.GetRefreshTokenInteractor
import com.leinardi.forlago.core.android.interactor.account.InvalidateAccessTokenInteractor
import com.leinardi.forlago.core.android.interactor.account.InvalidateRefreshTokenInteractor
import com.leinardi.forlago.core.android.interactor.account.PeekAccessTokenInteractor
import com.leinardi.forlago.core.navigation.ForlagoNavigator
import com.leinardi.forlago.core.navigation.destination.account.AccountAuthenticatorDestination
import com.leinardi.forlago.core.network.interactor.account.RemoveAccountsInteractor
import com.leinardi.forlago.core.ui.base.BaseViewModel
import com.leinardi.forlago.feature.account.ui.debug.AccountDebugContract.Effect
import com.leinardi.forlago.feature.account.ui.debug.AccountDebugContract.Event
import com.leinardi.forlago.feature.account.ui.debug.AccountDebugContract.State
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
                Event.OnGetAccessTokenClicked -> getAccessToken()
                Event.OnInvalidateAccessTokenClicked -> invalidateAccessToken()
                Event.OnInvalidateRefreshTokenClicked -> invalidateRefreshToken()
                Event.OnLogOutClicked -> logOut()
                Event.OnOpenSignInScreenClicked ->
                    forlagoNavigator.navigate(AccountAuthenticatorDestination.createRoute(viewState.value.accountName != null))
                Event.OnViewAttached -> updateState()
                Event.OnViewDetached -> Timber.d(">>> Detached")
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
        when (result) {
            is GetAccessTokenInteractor.Result.Success ->
                sendEffect { Effect.ShowSnackbar("Access token = ${result.accessToken}") }
            is GetAccessTokenInteractor.Result.Failure.AccountNotFound ->
                sendEffect { Effect.ShowSnackbar("Account not found") }
            is GetAccessTokenInteractor.Result.Failure.ReAuthenticationRequired ->
                sendEffect { Effect.ShowSnackbar("Authentication required") }
            is GetAccessTokenInteractor.Result.Failure.BadArgumentsError ->
                sendEffect { Effect.ShowSnackbar(checkNotNull(result.errorMessage)) }
            is GetAccessTokenInteractor.Result.Failure.AccountAuthenticatorError ->
                sendEffect { Effect.ShowSnackbar(checkNotNull(result.errorMessage)) }
            is GetAccessTokenInteractor.Result.Failure.NetworkError ->
                sendEffect { Effect.ShowSnackbar(checkNotNull(result.errorMessage)) }
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
