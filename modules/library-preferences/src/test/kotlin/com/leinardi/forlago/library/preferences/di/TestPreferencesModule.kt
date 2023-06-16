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

package com.leinardi.forlago.library.preferences.di

import com.leinardi.forlago.library.android.api.coroutine.CoroutineDispatchers
import com.leinardi.forlago.library.preferences.api.interactor.ReadEnvironmentInteractor
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module(includes = [PreferencesModule.BindModule::class])
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [PreferencesModule::class],
)
class TestPreferencesModule : PreferencesModule() {
    override fun readEnvironment(
        coroutineDispatchers: CoroutineDispatchers,
        readEnvironmentInteractor: ReadEnvironmentInteractor,
    ) = ReadEnvironmentInteractor.Environment.MOCK
}