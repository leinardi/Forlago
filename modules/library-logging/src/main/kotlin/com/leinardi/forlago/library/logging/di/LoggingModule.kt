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

package com.leinardi.forlago.library.logging.di

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.leinardi.forlago.library.logging.api.interactor.LogScreenViewInteractor
import com.leinardi.forlago.library.logging.interactor.LogScreenViewInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [LoggingModule.BindModule::class])
@InstallIn(SingletonComponent::class)
object LoggingModule {
    @Singleton
    @Provides
    fun provideAnalytics(): FirebaseAnalytics = Firebase.analytics

    @Module
    @InstallIn(SingletonComponent::class)
    internal interface BindModule {
        // Android Interactors
        @Binds
        fun bindCopyToClipboardInteractor(bind: LogScreenViewInteractorImpl): LogScreenViewInteractor
    }
}
