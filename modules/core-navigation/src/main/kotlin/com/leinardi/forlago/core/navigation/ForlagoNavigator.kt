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

package com.leinardi.forlago.core.navigation

import androidx.navigation.NavOptionsBuilder
import com.leinardi.forlago.core.navigation.destination.account.SignInDestination
import com.leinardi.forlago.core.navigation.destination.foo.FooDestination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForlagoNavigator @Inject constructor() {
    // A capacity > 0 is required to not lose an event sent before the nav host starts collecting (e.g. Add account from System settings)
    private val navigationEvents = Channel<NavigatorEvent>(capacity = Channel.CONFLATED)

    val destinations: Flow<NavigatorEvent> = navigationEvents.receiveAsFlow()

    /**
     * Attempts to navigate up in the navigation hierarchy. Suitable for when the user presses the
     * "Up" button marked with a left (or start)-facing arrow in the upper left (or starting)
     * corner of the app UI.
     *
     * The intended behavior of Up differs from Back when the user did not reach the current
     * destination from the application's own task. e.g. if the user is viewing a document or link
     * in the current app in an activity hosted on another app's task where the user clicked the
     * link. In this case the current activity (determined by the context used to create this
     * NavController) will be finished and the user will be taken to an appropriate destination
     * in this app on its own task.
     *
     * @return true if the navigation request was successfully delivered to the View, false otherwise
     */
    fun navigateUp(): Boolean = navigationEvents.trySend(NavigatorEvent.NavigateUp).isSuccess

    fun navigateHome(): Boolean = navigate(HOME_DESTINATION_ROUTE) {
        launchSingleTop = true
        popUpTo(0) { inclusive = true }
    }

    fun navigateToSignIn(reauthenticate: Boolean): Boolean = navigate(SignInDestination.createRoute(reauthenticate))

    /**
     * Attempts to pop the navigation controller's back stack. Analogous to when the user presses the system Back button.
     *
     * @return true if the navigation request was successfully delivered to the View, false otherwise
     */
    fun navigateBack(): Boolean = navigationEvents.trySend(NavigatorEvent.NavigateBack).isSuccess

    fun navigate(route: String, builder: NavOptionsBuilder.() -> Unit = { launchSingleTop = true }): Boolean =
        navigationEvents.trySend(NavigatorEvent.Directions(route, builder)).isSuccess

    companion object {
        val HOME_DESTINATION_ROUTE = FooDestination.createRoute()
    }
}
