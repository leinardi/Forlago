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

package com.leinardi.forlago.library.network.di

import com.leinardi.forlago.library.android.api.coroutine.CoroutineDispatchers
import com.leinardi.forlago.library.network.api.interactor.ReadEnvironmentInteractor
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module(includes = [NetworkModule.BindModule::class])
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class],
)
class TestNetworkModule : NetworkModule() {
    override fun readEnvironment(
        coroutineDispatchers: CoroutineDispatchers,
        readEnvironmentInteractor: ReadEnvironmentInteractor,
    ) = ReadEnvironmentInteractor.Environment.MOCK
}
