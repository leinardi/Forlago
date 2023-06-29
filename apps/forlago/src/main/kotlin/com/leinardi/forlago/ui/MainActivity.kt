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

package com.leinardi.forlago.ui

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED
import com.google.android.play.core.install.model.AppUpdateType
import com.leinardi.forlago.library.android.api.ext.getActivity
import com.leinardi.forlago.library.navigation.api.navigator.ForlagoNavigator
import com.leinardi.forlago.library.navigation.api.navigator.NavigatorEvent
import com.leinardi.forlago.library.ui.component.LocalMainScaffoldPadding
import com.leinardi.forlago.library.ui.component.LocalNavHostController
import com.leinardi.forlago.library.ui.component.ModalBottomSheetLayout
import com.leinardi.forlago.library.ui.component.Scaffold
import com.leinardi.forlago.library.ui.component.rememberBottomSheetNavigator
import com.leinardi.forlago.library.ui.theme.ForlagoTheme
import com.leinardi.forlago.navigation.addBottomSheetDestinations
import com.leinardi.forlago.navigation.addComposableDestinations
import com.leinardi.forlago.navigation.addDialogDestinations
import com.leinardi.forlago.ui.MainContract.Effect
import com.leinardi.forlago.ui.MainContract.Event
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {  // AppCompatActivity is needed to be able to toggle Day/Night programmatically
    @Inject lateinit var forlagoNavigator: ForlagoNavigator

    @Inject lateinit var appUpdateManager: AppUpdateManager

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()  // must be called before super.onCreate()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ForlagoTheme(isDynamicColor = viewModel.viewState.value.dynamicColors) {
                ForlagoMainScreen(
                    effectFlow = viewModel.effect,
                    startDestination = viewModel.viewState.value.startDestination,
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
                appUpdateInfo,
                appUpdateType,
                this,
                REQUEST_IN_APP_UPDATE,
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

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialNavigationApi::class)
@Suppress("ReusedModifierInstance")
@Composable
fun ForlagoMainScreen(
    effectFlow: Flow<Effect>,
    forlagoNavigator: ForlagoNavigator,
    startDestination: String,
    modifier: Modifier = Modifier,
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator(skipHalfExpanded = true)
    val navHostController = rememberNavController(bottomSheetNavigator)
    val activity = LocalContext.current.getActivity() as MainActivity
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(navHostController) {
        forlagoNavigator.destinations.onEach { event ->
            when (event) {
                is NavigatorEvent.Directions -> navHostController.navigate(
                    event.destination,
                    event.builder,
                ).also { Timber.d("Navigate to ${event.destination}") }
                is NavigatorEvent.HandleDeepLink -> navHostController.handleDeepLink(event.intent)
                is NavigatorEvent.NavigateUp -> navHostController.navigateUp().also { Timber.d("Navigate Up successful = $it") }
                is NavigatorEvent.NavigateBack -> navHostController.popBackStack().also { Timber.d("NavigateBack successful = $it") }
            }
        }.launchIn(this)
    }
    LaunchedEffect(effectFlow) {
        effectFlow.onEach { effect ->
            when (effect) {
                is Effect.FinishActivity -> activity.finish()
                is Effect.StartUpdateFlowForResult -> activity.startUpdateFlowForResult(effect.appUpdateInfo, effect.appUpdateType)
                is Effect.ShowSnackbarForCompleteUpdate -> launch { showSnackbarForCompleteUpdate(snackbarHostState, activity) }
                is Effect.ShowErrorSnackbar -> launch {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Indefinite,
                        actionLabel = activity.getString(effect.actionLabel),
                    ).also { snackbarResult ->
                        if (snackbarResult == SnackbarResult.ActionPerformed) {
                            snackbarHostState.currentSnackbarData?.dismiss()
                        }
                    }
                }
            }
        }.launchIn(this)
    }
    val mainScaffoldPadding: MutableState<PaddingValues> = remember { mutableStateOf(PaddingValues()) }
    CompositionLocalProvider(
        LocalMainScaffoldPadding provides mainScaffoldPadding,
        LocalNavHostController provides navHostController,
    ) {
        ModalBottomSheetLayout(
            bottomSheetNavigator = bottomSheetNavigator,
            modifier = modifier.imePadding(),
        ) {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
            ) { scaffoldPadding: PaddingValues ->
                mainScaffoldPadding.value = scaffoldPadding
                NavHost(
                    navController = navHostController,
                    startDestination = startDestination,
                    builder = {
                        addBottomSheetDestinations()
                        addComposableDestinations()
                        addDialogDestinations()
                    },
                )
            }
        }
    }
}

private suspend fun showSnackbarForCompleteUpdate(
    snackbarHostState: SnackbarHostState,
    activity: MainActivity,
) {
    snackbarHostState.showSnackbar(
        message = activity.getString(com.leinardi.forlago.library.i18n.R.string.i18n_app_update_snackbar_download_ready_label),
        duration = SnackbarDuration.Indefinite,
        actionLabel = activity.getString(com.leinardi.forlago.library.i18n.R.string.i18n_app_update_snackbar_download_ready_button),
    ).also { snackbarResult ->
        if (snackbarResult == SnackbarResult.ActionPerformed) {
            activity.completeAppUpdate()
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }
}
