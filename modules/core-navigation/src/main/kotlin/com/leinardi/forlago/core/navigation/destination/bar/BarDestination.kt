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

package com.leinardi.forlago.core.navigation.destination.bar

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.leinardi.forlago.core.navigation.BuildConfig
import com.leinardi.forlago.core.navigation.NavigationDestination

object BarDestination : NavigationDestination {
    const val TEXT_PARAM = "text"
    private const val BAR_ROUTE = "bar"

    override val arguments = listOf(
        navArgument(TEXT_PARAM) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        },
    )

    override val deepLinks = listOf(
        navDeepLink {
            uriPattern = "${BuildConfig.DEEP_LINK_SCHEMA}://$BAR_ROUTE/?$TEXT_PARAM={$TEXT_PARAM}"
        },
        navDeepLink {
            uriPattern = "${BuildConfig.DEEP_LINK_SCHEMA}://$BAR_ROUTE"
        },
    )

    override fun route() = "$BAR_ROUTE/?$TEXT_PARAM={$TEXT_PARAM}"

    fun createRoute(text: String) = "$BAR_ROUTE/?$TEXT_PARAM=$text"
}
