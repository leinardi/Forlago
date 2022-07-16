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

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.leinardi.forlago.library.android.coroutine.CoroutineDispatchers
import com.leinardi.forlago.library.android.di.App
import com.leinardi.forlago.library.android.di.User
import com.leinardi.forlago.library.preferences.interactor.ReadEnvironmentInteractor
import com.leinardi.forlago.library.preferences.repository.DataStoreRepository
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
    @App
    fun provideAppDataStoreRepository(application: Application) = DataStoreRepository(application, "app_preference_storage")

    @Provides
    @Singleton
    @User
    fun provideUserDataStoreRepository(application: Application) = DataStoreRepository(application, "user_preference_storage")

    @Provides
    @Singleton
    fun provideReadEnvironmentInteractorEnvironment(
        coroutineDispatchers: CoroutineDispatchers,
        readEnvironmentInteractor: ReadEnvironmentInteractor,
    ): ReadEnvironmentInteractor.Environment = readEnvironment(coroutineDispatchers, readEnvironmentInteractor)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    protected open fun readEnvironment(
        coroutineDispatchers: CoroutineDispatchers,
        readEnvironmentInteractor: ReadEnvironmentInteractor,
    ): ReadEnvironmentInteractor.Environment = runBlocking(coroutineDispatchers.io) { readEnvironmentInteractor() }
}
