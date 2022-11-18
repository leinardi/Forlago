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

package com.leinardi.forlago.library.preferences.interactor

import com.leinardi.forlago.library.preferences.api.di.User
import com.leinardi.forlago.library.preferences.api.interactor.StoreThemeInteractor
import com.leinardi.forlago.library.preferences.api.repository.DataStoreRepository
import com.leinardi.forlago.library.preferences.interactor.GetThemeFlowInteractorImpl.Companion.THEME_PREF_KEY
import com.leinardi.forlago.library.ui.api.NightMode
import javax.inject.Inject

internal class StoreThemeInteractorImpl @Inject constructor(
    @User private val userDataStoreRepository: DataStoreRepository,
) : StoreThemeInteractor {
    override suspend operator fun invoke(nightMode: NightMode) {
        userDataStoreRepository.storeValue(THEME_PREF_KEY, nightMode.intValue)
    }
}
