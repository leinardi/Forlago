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

import androidx.datastore.preferences.core.booleanPreferencesKey
import com.leinardi.forlago.library.preferences.api.di.User
import com.leinardi.forlago.library.preferences.api.interactor.GetMaterialYouFlowInteractor
import com.leinardi.forlago.library.preferences.api.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetMaterialYouFlowInteractorImpl @Inject constructor(
    @User private val userDataStoreRepository: DataStoreRepository,
) : GetMaterialYouFlowInteractor {
    override operator fun invoke(): Flow<Boolean> = userDataStoreRepository.observeValue(MATERIAL_YOU_PREF_KEY).map { it == true }

    companion object {
        val MATERIAL_YOU_PREF_KEY = booleanPreferencesKey("materialYou")
    }
}
