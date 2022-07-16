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

package com.leinardi.forlago.library.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink

abstract class NavigationDestination {
    open val arguments: List<NamedNavArgument>
        get() = emptyList()

    open val deepLinks: List<NavDeepLink>
        get() = emptyList()

    abstract val route: String

    abstract class Result {
        abstract val id: Long
        var consumed: Boolean = false

        companion object {
            val ID
                get() = System.currentTimeMillis()
        }
    }
}
