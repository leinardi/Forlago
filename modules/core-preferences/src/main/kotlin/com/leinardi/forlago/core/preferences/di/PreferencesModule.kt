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

package com.leinardi.forlago.core.preferences.di

import androidx.annotation.VisibleForTesting
import com.leinardi.forlago.core.android.coroutine.CoroutineDispatchers
import com.leinardi.forlago.core.preferences.interactor.GetEnvironmentInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class PreferencesModule {
    @Provides
    @Singleton
    fun provideGetEnvironmentInteractorEnvironment(
        coroutineDispatchers: CoroutineDispatchers,
        getEnvironmentInteractor: GetEnvironmentInteractor,
    ): GetEnvironmentInteractor.Environment = getEnvironment(coroutineDispatchers, getEnvironmentInteractor)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    protected open fun getEnvironment(
        coroutineDispatchers: CoroutineDispatchers,
        getEnvironmentInteractor: GetEnvironmentInteractor,
    ): GetEnvironmentInteractor.Environment = runBlocking(coroutineDispatchers.io) { getEnvironmentInteractor() }
}
