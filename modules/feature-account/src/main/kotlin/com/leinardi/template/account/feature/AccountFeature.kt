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

package com.leinardi.template.account.feature

import android.accounts.AccountManager
import android.content.Intent
import androidx.compose.runtime.Composable
import com.leinardi.template.account.authenticator.AccountAuthenticator
import com.leinardi.template.account.ui.AccountAuthenticatorScreen
import com.leinardi.template.account.ui.debug.AccountDebugPage
import com.leinardi.template.feature.Feature
import com.leinardi.template.navigation.NavigationDestination
import com.leinardi.template.navigation.TemplateNavigator
import com.leinardi.template.navigation.destination.account.AccountAuthenticatorDestination

class AccountFeature(
    val mainActivityIntent: Intent,
    private val navigator: TemplateNavigator,
) : Feature() {
    override val id = "Account"

    override val composableDestinations: Map<NavigationDestination, @Composable () -> Unit> = mapOf(
        AccountAuthenticatorDestination to { AccountAuthenticatorScreen() },
    )

    override val debugComposable: @Composable () -> Unit = { AccountDebugPage() }

    override val handleIntent: suspend (Intent) -> Unit = { intent ->
        if (intent.hasExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE)) {
            val isNewAccount = intent.getBooleanExtra(AccountAuthenticator.KEY_IS_NEW_ACCOUNT, false)
            navigator.navigate(AccountAuthenticatorDestination.createRoute(!isNewAccount)) {
                launchSingleTop = true
                popUpTo(0) { inclusive = true }
            }
        }
    }
}
