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

package com.leinardi.template.navigation.destination.debug

import androidx.navigation.navDeepLink
import com.leinardi.template.navigation.BuildConfig
import com.leinardi.template.navigation.NavigationDestination

object DebugDestination : NavigationDestination {
    private const val DEBUG_ROUTE = "debug"

    override val deepLinks = listOf(
        navDeepLink {
            uriPattern = "${BuildConfig.DEEP_LINK_SCHEMA}://$DEBUG_ROUTE"
        }
    )

    override fun route() = DEBUG_ROUTE
}
