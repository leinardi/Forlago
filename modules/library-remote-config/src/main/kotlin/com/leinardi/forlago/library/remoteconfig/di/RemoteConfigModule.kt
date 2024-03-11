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

package com.leinardi.forlago.library.remoteconfig.di

import com.leinardi.forlago.library.remoteconfig.api.interactor.GetFeatureFlagInteractor
import com.leinardi.forlago.library.remoteconfig.api.interactor.GetKillSwitchStreamInteractor
import com.leinardi.forlago.library.remoteconfig.interactor.GetFeatureFlagInteractorImpl
import com.leinardi.forlago.library.remoteconfig.interactor.GetKillSwitchStreamInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [RemoteConfigModule.BindModule::class])
@InstallIn(SingletonComponent::class)
object RemoteConfigModule {
    @Module
    @InstallIn(SingletonComponent::class)
    internal interface BindModule {
        @Binds
        fun bindGetFeatureFlagInteractor(bind: GetFeatureFlagInteractorImpl): GetFeatureFlagInteractor

        @Binds
        fun bindGetKillSwitchStreamInteractor(bind: GetKillSwitchStreamInteractorImpl): GetKillSwitchStreamInteractor
    }
}
