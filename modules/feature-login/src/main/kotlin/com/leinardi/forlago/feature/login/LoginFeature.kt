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

package com.leinardi.forlago.feature.login

import android.accounts.AccountManager
import android.content.Intent
import androidx.compose.runtime.Composable
import com.leinardi.forlago.feature.login.api.destination.LogInDestination
import com.leinardi.forlago.feature.login.ui.LogInScreen
import com.leinardi.forlago.library.feature.Feature
import com.leinardi.forlago.library.navigation.api.destination.NavigationDestination
import com.leinardi.forlago.library.navigation.api.navigator.ForlagoNavigator

class LoginFeature : Feature() {
    override val id = "Login"

    override val composableDestinations: Map<NavigationDestination, @Composable () -> Unit> = mapOf(
        LogInDestination to { LogInScreen() },
    )

    override val handleIntent: suspend (Intent, ForlagoNavigator) -> Boolean = { intent, navigator ->
        if (intent.isNewAccount()) {
            handleNewAccountAdded(navigator)
        } else {
            false
        }
    }

    private fun Intent.isNewAccount(): Boolean = hasExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE)

    private fun handleNewAccountAdded(navigator: ForlagoNavigator): Boolean {
        navigator.navigateToLogin {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
        return true
    }
}
