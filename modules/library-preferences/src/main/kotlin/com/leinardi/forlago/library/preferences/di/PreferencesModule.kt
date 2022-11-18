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
import com.leinardi.forlago.library.android.api.coroutine.CoroutineDispatchers
import com.leinardi.forlago.library.preferences.api.di.App
import com.leinardi.forlago.library.preferences.api.di.User
import com.leinardi.forlago.library.preferences.api.interactor.GetMaterialYouFlowInteractor
import com.leinardi.forlago.library.preferences.api.interactor.GetThemeFlowInteractor
import com.leinardi.forlago.library.preferences.api.interactor.ReadCertificatePinningIsEnabledInteractor
import com.leinardi.forlago.library.preferences.api.interactor.ReadEnvironmentInteractor
import com.leinardi.forlago.library.preferences.api.interactor.StoreCertificatePinningIsEnabledInteractor
import com.leinardi.forlago.library.preferences.api.interactor.StoreEnvironmentInteractor
import com.leinardi.forlago.library.preferences.api.interactor.StoreMaterialYouInteractor
import com.leinardi.forlago.library.preferences.api.interactor.StoreThemeInteractor
import com.leinardi.forlago.library.preferences.api.repository.DataStoreRepository
import com.leinardi.forlago.library.preferences.interactor.GetMaterialYouFlowInteractorImpl
import com.leinardi.forlago.library.preferences.interactor.GetThemeFlowInteractorImpl
import com.leinardi.forlago.library.preferences.interactor.ReadCertificatePinningIsEnabledInteractorImpl
import com.leinardi.forlago.library.preferences.interactor.ReadEnvironmentInteractorImpl
import com.leinardi.forlago.library.preferences.interactor.StoreCertificatePinningIsEnabledInteractorImpl
import com.leinardi.forlago.library.preferences.interactor.StoreEnvironmentInteractorImpl
import com.leinardi.forlago.library.preferences.interactor.StoreMaterialYouInteractorImpl
import com.leinardi.forlago.library.preferences.interactor.StoreThemeInteractorImpl
import com.leinardi.forlago.library.preferences.repository.DataStoreRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module(includes = [PreferencesModule.BindModule::class])
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

    @Module
    @InstallIn(SingletonComponent::class)
    internal interface BindModule {
        @Binds
        fun bindGetMaterialYouFlowInteractor(bind: GetMaterialYouFlowInteractorImpl): GetMaterialYouFlowInteractor

        @Binds
        fun bindGetThemeFlowInteractor(bind: GetThemeFlowInteractorImpl): GetThemeFlowInteractor

        @Binds
        fun bindReadCertificatePinningIsEnabledInteractor(bind: ReadCertificatePinningIsEnabledInteractorImpl):
            ReadCertificatePinningIsEnabledInteractor

        @Binds
        fun bindReadEnvironmentInteractor(bind: ReadEnvironmentInteractorImpl): ReadEnvironmentInteractor

        @Binds
        fun bindStoreCertificatePinningIsEnabledInteractor(bind: StoreCertificatePinningIsEnabledInteractorImpl):
            StoreCertificatePinningIsEnabledInteractor

        @Binds
        fun bindStoreEnvironmentInteractor(bind: StoreEnvironmentInteractorImpl): StoreEnvironmentInteractor

        @Binds
        fun bindStoreMaterialYouInteractor(bind: StoreMaterialYouInteractorImpl): StoreMaterialYouInteractor

        @Binds
        fun bindStoreThemeInteractor(bind: StoreThemeInteractorImpl): StoreThemeInteractor
    }
}
