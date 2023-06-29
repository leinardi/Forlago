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

package com.leinardi.forlago.library.ui.interactor

import androidx.datastore.preferences.core.intPreferencesKey
import com.leinardi.forlago.library.preferences.api.di.User
import com.leinardi.forlago.library.preferences.api.repository.DataStoreRepository
import com.leinardi.forlago.library.ui.api.NightMode
import com.leinardi.forlago.library.ui.api.interactor.GetThemeFlowInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetThemeFlowInteractorImpl @Inject constructor(
    @User private val userDataStoreRepository: DataStoreRepository,
) : GetThemeFlowInteractor {
    override operator fun invoke(): Flow<NightMode> =
        userDataStoreRepository.observeValue(THEME_PREF_KEY).map { if (it == null) NightMode.DEFAULT else NightMode.fromIntValue(it) }

    companion object {
        internal val THEME_PREF_KEY = intPreferencesKey("theme")
    }
}
