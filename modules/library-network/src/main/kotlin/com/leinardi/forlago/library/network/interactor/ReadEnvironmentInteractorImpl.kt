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

package com.leinardi.forlago.library.network.interactor

import androidx.datastore.preferences.core.stringPreferencesKey
import com.leinardi.forlago.library.network.api.interactor.ReadEnvironmentInteractor
import com.leinardi.forlago.library.network.api.interactor.ReadEnvironmentInteractor.Environment
import com.leinardi.forlago.library.preferences.api.di.App
import com.leinardi.forlago.library.preferences.api.repository.DataStoreRepository
import javax.inject.Inject

internal class ReadEnvironmentInteractorImpl @Inject constructor(
    @App private val dataStoreRepository: DataStoreRepository,
) : ReadEnvironmentInteractor {
    override suspend operator fun invoke(): Environment =
        Environment.valueOf(dataStoreRepository.readValue(ENVIRONMENT_PREF_KEY, DEFAULT_ENVIRONMENT.name))

    companion object {
        val DEFAULT_ENVIRONMENT = Environment.STAGE
        val ENVIRONMENT_PREF_KEY = stringPreferencesKey("environment")
    }
}
