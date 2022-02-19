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

package com.leinardi.forlago.core.android.di

import android.accounts.AccountManager
import android.app.Application
import com.leinardi.forlago.core.android.coroutine.CoroutineDispatchers
import com.leinardi.forlago.core.android.encryption.CryptoHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AndroidModule {
    @Provides
    @Singleton
    fun provideAccountManager(application: Application): AccountManager = AccountManager.get(application)

    @Provides
    @Singleton
    fun provideCoroutineDispatchers(): CoroutineDispatchers = CoroutineDispatchers()

    @Provides
    @Singleton
    fun provideCryptoHelper(
        dispatchers: CoroutineDispatchers,
        application: Application,
    ): CryptoHelper = runBlocking {
        withContext(dispatchers.io) {
            CryptoHelper.Builder(application).build()
        }
    }
}
