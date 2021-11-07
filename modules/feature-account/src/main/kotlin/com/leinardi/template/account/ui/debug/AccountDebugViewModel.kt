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

package com.leinardi.template.account.ui.debug

import androidx.lifecycle.viewModelScope
import com.leinardi.template.account.interactor.GetAccessTokenExpiryInteractor
import com.leinardi.template.account.interactor.GetAccessTokenInteractor
import com.leinardi.template.account.interactor.GetAccountInteractor
import com.leinardi.template.account.interactor.GetRefreshTokenInteractor
import com.leinardi.template.account.interactor.InvalidateAccessTokenInteractor
import com.leinardi.template.account.interactor.InvalidateRefreshTokenInteractor
import com.leinardi.template.account.interactor.PeekAccessTokenInteractor
import com.leinardi.template.account.ui.debug.AccountDebugContract.Effect
import com.leinardi.template.account.ui.debug.AccountDebugContract.Event
import com.leinardi.template.account.ui.debug.AccountDebugContract.State
import com.leinardi.template.navigation.TemplateNavigator
import com.leinardi.template.navigation.destination.account.AccountAuthenticatorDestination
import com.leinardi.template.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AccountDebugViewModel @Inject constructor(
    private val getAccessTokenExpiryInteractor: GetAccessTokenExpiryInteractor,
    private val getAccessTokenInteractor: GetAccessTokenInteractor,
    private val getAccountInteractor: GetAccountInteractor,
    private val getRefreshTokenInteractor: GetRefreshTokenInteractor,
    private val invalidateAccessTokenInteractor: InvalidateAccessTokenInteractor,
    private val invalidateRefreshTokenInteractor: InvalidateRefreshTokenInteractor,
    private val peekAccessTokenInteractor: PeekAccessTokenInteractor,
    private val templateNavigator: TemplateNavigator,
) : BaseViewModel<Event, State, Effect>() {
    override fun provideInitialState() = State(null, null, null, null)

    override fun handleEvent(event: Event) {
        viewModelScope.launch {
            when (event) {
                Event.OnInvalidateAccessTokenClicked -> invalidateAccessToken()
                Event.OnInvalidateRefreshTokenClicked -> invalidateRefreshToken()
                Event.OnOpenSignInScreenClicked ->
                    templateNavigator.navigate(AccountAuthenticatorDestination.createRoute(viewState.value.accountName != null))
                Event.OnRefreshAccessTokenClicked -> refreshAccessToken()
                Event.OnViewAttached -> updateState { createState() }
                Event.OnViewDetached -> Timber.d(">>> Detached")
            }
        }
    }

    private suspend fun invalidateAccessToken() {
        invalidateAccessTokenInteractor()
        updateState { createState() }
    }

    private suspend fun invalidateRefreshToken() {
        invalidateRefreshTokenInteractor()
        updateState { createState() }
    }

    private suspend fun refreshAccessToken() {
        val result = getAccessTokenInteractor()
        Timber.d("getAccessToken result = $result")
        updateState { createState() }
        when (result) {
            is GetAccessTokenInteractor.Result.Success ->
                sendEffect { Effect.ShowSnackbar("Access token = ${result.accessToken}") }
            is GetAccessTokenInteractor.Result.Failure.AccountNotFound ->
                sendEffect { Effect.ShowSnackbar("Account not found") }
            is GetAccessTokenInteractor.Result.Failure.AuthenticationRequired ->
                sendEffect { Effect.ShowSnackbar("Authentication required") }
            is GetAccessTokenInteractor.Result.Failure.BadArguments ->
                sendEffect { Effect.ShowSnackbar(checkNotNull(result.errorMessage)) }
            is GetAccessTokenInteractor.Result.Failure.BadRequest ->
                sendEffect { Effect.ShowSnackbar(checkNotNull(result.errorMessage)) }
            is GetAccessTokenInteractor.Result.Failure.NetworkError ->
                sendEffect { Effect.ShowSnackbar(checkNotNull(result.errorMessage)) }
        }
    }

    private fun createState(): State {
        val account = getAccountInteractor()
        val refreshToken = getRefreshTokenInteractor()
        val accessToken = peekAccessTokenInteractor()
        val accessTokenExpiration = getAccessTokenExpiryInteractor()
        return State(account?.name, refreshToken, accessToken, accessTokenExpiration)
    }
}
