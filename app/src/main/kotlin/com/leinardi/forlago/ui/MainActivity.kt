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

package com.leinardi.forlago.ui

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED
import com.google.android.play.core.install.model.AppUpdateType
import com.leinardi.forlago.R
import com.leinardi.forlago.core.android.ext.getActivity
import com.leinardi.forlago.core.navigation.ForlagoNavigator
import com.leinardi.forlago.core.navigation.NavigatorEvent
import com.leinardi.forlago.core.ui.component.LocalNavHostController
import com.leinardi.forlago.core.ui.component.LocalSnackbarHostState
import com.leinardi.forlago.core.ui.theme.ForlagoTheme
import com.leinardi.forlago.navigation.addComposableDestinations
import com.leinardi.forlago.navigation.addDialogDestinations
import com.leinardi.forlago.ui.MainContract.Effect
import com.leinardi.forlago.ui.MainContract.Event
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {  // AppCompatActivity is needed to be able to toggle Day/Night programmatically
    @Inject lateinit var forlagoNavigator: ForlagoNavigator
    @Inject lateinit var appUpdateManager: AppUpdateManager

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            ForlagoTheme {
                ForlagoMainScreen(
                    effectFlow = viewModel.effect,
                    startDestination = viewModel.viewState.value.startDestination,
                    navHostController = rememberNavController(),
                    forlagoNavigator = forlagoNavigator,
                )
            }
        }
        viewModel.onUiEvent(Event.OnIntentReceived(intent))
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        viewModel.onUiEvent(Event.OnIntentReceived(intent, true))
    }

    override fun onResume() {
        super.onResume()
        viewModel.onUiEvent(Event.OnShown)
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")  // https://stackoverflow.com/q/65409381/293878
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IN_APP_UPDATE) {
            when (resultCode) {
                RESULT_CANCELED -> viewModel.onUiEvent(Event.OnInAppUpdateCancelled)
                RESULT_IN_APP_UPDATE_FAILED -> viewModel.onUiEvent(Event.OnInAppUpdateFailed)
            }
        }
    }

    fun startUpdateFlowForResult(
        appUpdateInfo: AppUpdateInfo,
        @AppUpdateType appUpdateType: Int,
    ) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo, appUpdateType, this, REQUEST_IN_APP_UPDATE,
            )
        } catch (e: IntentSender.SendIntentException) {
            Timber.e(e)
        }
    }

    fun completeAppUpdate() {
        appUpdateManager.completeUpdate()
    }

    companion object {
        private const val REQUEST_IN_APP_UPDATE = 9754
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ForlagoMainScreen(
    effectFlow: Flow<Effect>,
    navHostController: NavHostController,
    forlagoNavigator: ForlagoNavigator,
    startDestination: String,
) {
    val activity = LocalContext.current.getActivity() as MainActivity
    val keyboardController = LocalSoftwareKeyboardController.current
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(navHostController) {
        forlagoNavigator.destinations.onEach { event ->
            Timber.d("backQueue = ${navHostController.backQueue.map { "route = ${it.destination.route}" }}")
            keyboardController?.hide()
            when (event) {
                is NavigatorEvent.Directions -> navHostController.navigate(
                    event.destination,
                    event.builder,
                ).also { Timber.d("Navigate to ${event.destination}") }
                is NavigatorEvent.HandleDeepLink -> navHostController.handleDeepLink(event.intent)
                is NavigatorEvent.NavigateBack -> activity.onBackPressed().also { Timber.d("NavigateBack") }
                is NavigatorEvent.NavigateUp -> Timber.d("NavigateUp successful = ${navHostController.navigateUp()}")
            }
        }.launchIn(this)
    }
    LaunchedEffect(effectFlow) {
        effectFlow.onEach { effect ->
            when (effect) {
                is Effect.FinishActivity -> activity.finish()
                is Effect.StartUpdateFlowForResult -> activity.startUpdateFlowForResult(effect.appUpdateInfo, effect.appUpdateType)
                is Effect.ShowSnackbarForCompleteUpdate -> showSnackbarForCompleteUpdate(scaffoldState.snackbarHostState, activity)
                is Effect.ShowErrorSnackbar -> scaffoldState.snackbarHostState.showSnackbar(
                    message = effect.message,
                    duration = SnackbarDuration.Indefinite,
                    actionLabel = activity.getString(effect.actionLabel),
                ).also { snackbarResult ->
                    if (snackbarResult == SnackbarResult.ActionPerformed) {
                        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    }
                }
            }
        }.launchIn(this)
    }
    CompositionLocalProvider(
        LocalNavHostController provides navHostController,
        LocalSnackbarHostState provides scaffoldState.snackbarHostState,
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            content = { scaffoldPadding ->
                NavHost(
                    // check https://google.github.io/accompanist/navigation-animation/
                    navController = navHostController,
                    startDestination = startDestination,
                    modifier = Modifier
                        .padding(
                            top = scaffoldPadding.calculateTopPadding(),
                            bottom = scaffoldPadding.calculateBottomPadding(),
                        ),
                    builder = {
                        addComposableDestinations()
                        addDialogDestinations()
                    },
                )
            },
        )
    }
}

private suspend fun showSnackbarForCompleteUpdate(
    snackbarHostState: SnackbarHostState,
    activity: MainActivity,
) {
    snackbarHostState.showSnackbar(
        message = activity.getString(R.string.i18n_app_update_snackbar_download_ready_label),
        duration = SnackbarDuration.Indefinite,
        actionLabel = activity.getString(R.string.i18n_app_update_snackbar_download_ready_button),
    ).also { snackbarResult ->
        if (snackbarResult == SnackbarResult.ActionPerformed) {
            activity.completeAppUpdate()
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }
}
