/*
 * Copyright 2023 Roberto Leinardi.
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

package com.leinardi.forlago.feature.account

import android.accounts.AccountManager
import android.content.Intent
import androidx.compose.runtime.Composable
import com.leinardi.forlago.feature.account.api.interactor.account.RemoveAccountsInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.InvalidateAccessTokenInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.InvalidateRefreshTokenInteractor
import com.leinardi.forlago.feature.account.ui.SignInScreen
import com.leinardi.forlago.feature.account.ui.debug.AccountDebugPage
import com.leinardi.forlago.library.android.api.interactor.android.DeleteWebViewDataInteractor
import com.leinardi.forlago.library.feature.Feature
import com.leinardi.forlago.library.feature.FeatureLifecycle
import com.leinardi.forlago.library.navigation.api.destination.NavigationDestination
import com.leinardi.forlago.library.navigation.api.destination.account.SignInDestination
import com.leinardi.forlago.library.navigation.api.navigator.ForlagoNavigator

class AccountFeature(
    private val deleteWebViewDataInteractor: DeleteWebViewDataInteractor,
    private val invalidateAccessTokenInteractor: InvalidateAccessTokenInteractor,
    private val invalidateRefreshTokenInteractor: InvalidateRefreshTokenInteractor,
    private val navigator: ForlagoNavigator,
    private val removeAccountsInteractor: RemoveAccountsInteractor,
    val mainActivityIntent: Intent,
) : Feature() {
    override val id = "Account"

    override val composableDestinations: Map<NavigationDestination, @Composable () -> Unit> = mapOf(
        SignInDestination to { SignInScreen() },
    )

    override val debugComposable: @Composable () -> Unit = { AccountDebugPage() }

    override val handleIntent: suspend (Intent) -> Boolean = { intent ->
        if (intent.hasExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE)) {
            val isNewAccount = intent.getBooleanExtra(AccountAuthenticatorConfig.KEY_IS_NEW_ACCOUNT, false)
            navigator.navigate(SignInDestination.get(!isNewAccount)) {
                launchSingleTop = true
                popUpTo(0) { inclusive = true }
            }
            true
        } else {
            false
        }
    }

    override val featureLifecycle: FeatureLifecycle = FeatureLifecycle(
        onSignOut = {
            invalidateAccessTokenInteractor()
            invalidateRefreshTokenInteractor()
            removeAccountsInteractor()
            deleteWebViewDataInteractor()
        },
    )
}
