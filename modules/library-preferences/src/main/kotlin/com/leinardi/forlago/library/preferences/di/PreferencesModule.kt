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

package com.leinardi.forlago.library.preferences.di

import android.app.Application
import com.leinardi.forlago.library.preferences.api.di.App
import com.leinardi.forlago.library.preferences.api.di.User
import com.leinardi.forlago.library.preferences.api.repository.DataStoreRepository
import com.leinardi.forlago.library.preferences.repository.DataStoreRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class PreferencesModule {
    @Provides
    @Singleton
    @App
    fun provideAppDataStoreRepository(application: Application): DataStoreRepository =
        DataStoreRepositoryImpl(application, "app_preference_storage")

    @Provides
    @Singleton
    @User
    fun provideUserDataStoreRepository(application: Application): DataStoreRepository =
        DataStoreRepositoryImpl(application, "user_preference_storage")
}
