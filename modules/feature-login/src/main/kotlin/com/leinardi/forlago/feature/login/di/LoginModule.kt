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

package com.leinardi.forlago.feature.login.di

import com.leinardi.forlago.feature.login.LoginFeature
import com.leinardi.forlago.feature.login.api.interactor.IsLogInInProgressInteractor
import com.leinardi.forlago.feature.login.api.interactor.LogInInteractor
import com.leinardi.forlago.feature.login.interactor.IsLogInInProgressInteractorImpl
import com.leinardi.forlago.feature.login.interactor.LogInInteractorImpl
import com.leinardi.forlago.library.feature.Feature
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module(includes = [LoginModule.BindModule::class])
@InstallIn(SingletonComponent::class)
object LoginModule {
    @Provides
    @Singleton
    @IntoSet
    fun provideLoginFeature(): Feature = LoginFeature()

    @Module
    @InstallIn(SingletonComponent::class)
    internal interface BindModule {
        @Binds
        fun bindLogInInteractor(bind: LogInInteractorImpl): LogInInteractor

        @Binds
        fun bindIsLogInInProgressInteractor(bind: IsLogInInProgressInteractorImpl): IsLogInInProgressInteractor
    }
}
