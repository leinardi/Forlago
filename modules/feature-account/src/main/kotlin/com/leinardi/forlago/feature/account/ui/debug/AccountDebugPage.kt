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

package com.leinardi.forlago.feature.account.ui.debug

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leinardi.forlago.core.android.ext.toLongDateTimeString
import com.leinardi.forlago.core.ui.component.LocalSnackbarHostState
import com.leinardi.forlago.core.ui.component.SettingsMenuLink
import com.leinardi.forlago.feature.account.ui.debug.AccountDebugContract.Effect
import com.leinardi.forlago.feature.account.ui.debug.AccountDebugContract.State
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun AccountDebugPage(viewModel: AccountDebugViewModel = hiltViewModel()) {
    DisposableEffect(viewModel) {
        viewModel.onUiEvent(AccountDebugContract.Event.OnViewAttached)
        onDispose { viewModel.onUiEvent(AccountDebugContract.Event.OnViewDetached) }
    }
    AccountDebugPage(
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        sendEvent = { viewModel.onUiEvent(it) },
    )
}

@Composable
fun AccountDebugPage(
    state: State,
    effectFlow: Flow<Effect>,
    sendEvent: (event: AccountDebugContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = LocalSnackbarHostState.current
    LaunchedEffect(effectFlow) {
        effectFlow.onEach { effect ->
            when (effect) {
                is Effect.ShowSnackbar -> snackbarHostState.showSnackbar(
                    message = effect.message,
                    duration = SnackbarDuration.Short,
                )
            }
        }.collect()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 8.dp),
    ) {
        AccountInfo(state)
        EventButtons(sendEvent)
    }
}

@Composable
private fun EventButtons(sendEvent: (event: AccountDebugContract.Event) -> Unit) {
    Button(
        onClick = { sendEvent(AccountDebugContract.Event.OnGetAccessTokenClicked) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text("Get access token")
    }
    Button(
        onClick = { sendEvent(AccountDebugContract.Event.OnInvalidateAccessTokenClicked) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text("Invalidate access token")
    }
    Button(
        onClick = { sendEvent(AccountDebugContract.Event.OnInvalidateRefreshTokenClicked) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text("Invalidate refresh token")
    }
    Button(
        onClick = { sendEvent(AccountDebugContract.Event.OnOpenSignInScreenClicked) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text("Open Sign In screen")
    }
}

@Composable
private fun AccountInfo(state: State) {
    SettingsMenuLink(
        title = { Text(text = "Account name") },
        subtitle = { Text(text = state.accountName.orEmpty()) },
        enabled = state.accountName != null,
    )
    SettingsMenuLink(
        title = { Text(text = "Refresh token") },
        subtitle = { Text(text = state.refreshToken.orEmpty()) },
        enabled = state.refreshToken != null,
    )
    SettingsMenuLink(
        title = { Text(text = "Access token") },
        subtitle = { Text(text = state.accessToken.orEmpty()) },
        enabled = state.accessToken != null,
    )
    val expiration = state.accessTokenExpiry?.toLongDateTimeString().orEmpty()
    val expiryColor = if (System.currentTimeMillis() - (state.accessTokenExpiry ?: 0) < 0) {
        Color.Unspecified
    } else {
        MaterialTheme.colors.error
    }
    SettingsMenuLink(
        title = { Text(text = "Access token expiration") },
        subtitle = { Text(text = expiration, color = expiryColor) },
        enabled = state.accessTokenExpiry != null,
    )
}

@Preview
@Composable
fun AccountDebugPreview() {
    AccountDebugPage(
        State("AccountName", "refreshToken", "accessToken", System.currentTimeMillis()),
        Channel<Effect>().receiveAsFlow(),
        {},
    )
}
