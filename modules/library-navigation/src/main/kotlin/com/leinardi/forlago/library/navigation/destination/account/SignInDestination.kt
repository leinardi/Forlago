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

package com.leinardi.forlago.library.navigation.destination.account

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.leinardi.forlago.library.navigation.NavigationDestination

object SignInDestination : NavigationDestination() {
    const val REAUTHENTICATE_PARAM = "reauthenticate"

    private const val ROUTE = "signin"

    override val arguments = listOf(
        navArgument(REAUTHENTICATE_PARAM) {
            type = NavType.BoolType
            defaultValue = false
        },
    )

    override val route = "$ROUTE/?$REAUTHENTICATE_PARAM={$REAUTHENTICATE_PARAM}"

    fun createRoute(reauthenticate: Boolean = false) = "$ROUTE/?$REAUTHENTICATE_PARAM=$reauthenticate"
}
