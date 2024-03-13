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

package com.leinardi.forlago.feature.account

import android.content.Intent
import androidx.compose.runtime.Composable
import com.leinardi.forlago.feature.account.api.interactor.account.RemoveAccountsInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.InvalidateAccessTokenInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.InvalidateRefreshTokenInteractor
import com.leinardi.forlago.feature.account.ui.debug.AccountDebugPage
import com.leinardi.forlago.library.android.api.interactor.android.DeleteWebViewDataInteractor
import com.leinardi.forlago.library.feature.Feature
import com.leinardi.forlago.library.feature.FeatureLifecycle
import com.leinardi.forlago.library.preferences.api.di.User
import com.leinardi.forlago.library.preferences.api.repository.DataStoreRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class AccountFeature @AssistedInject constructor(
    @Assisted val mainActivityIntent: Intent,
    private val deleteWebViewDataInteractor: DeleteWebViewDataInteractor,
    private val invalidateAccessTokenInteractor: InvalidateAccessTokenInteractor,
    private val invalidateRefreshTokenInteractor: InvalidateRefreshTokenInteractor,
    private val removeAccountsInteractor: RemoveAccountsInteractor,
    @User private val userDataStoreRepository: DataStoreRepository,
) : Feature() {
    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted mainActivityIntent: Intent,
        ): AccountFeature
    }

    override val id = "Account"

    override val debugComposable: @Composable () -> Unit = { AccountDebugPage() }

    override val featureLifecycle: FeatureLifecycle = FeatureLifecycle(
        onLogout = {
            invalidateAccessTokenInteractor()
            invalidateRefreshTokenInteractor()
            removeAccountsInteractor()
            deleteWebViewDataInteractor()
            userDataStoreRepository.clearDataStore()
        },
    )
}
