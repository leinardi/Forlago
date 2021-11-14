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

package com.leinardi.template.core.navigation.destination.account

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.leinardi.template.core.navigation.NavigationDestination

object AccountAuthenticatorDestination : NavigationDestination {
    const val RELOGIN_PARAM = "relogin"

    private const val ACCOUNT_AUTHENTICATOR_ROUTE = "authenticator"

    override val arguments = listOf(
        navArgument(RELOGIN_PARAM) {
            type = NavType.BoolType
            defaultValue = false
        },
    )

    override fun route() = "$ACCOUNT_AUTHENTICATOR_ROUTE/?$RELOGIN_PARAM={$RELOGIN_PARAM}"

    fun createRoute(relogin: Boolean = false) = "$ACCOUNT_AUTHENTICATOR_ROUTE/?$RELOGIN_PARAM=$relogin"
}
