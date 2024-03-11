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

package com.leinardi.forlago.library.android.di

import android.accounts.AccountManager
import android.app.Application
import androidx.annotation.VisibleForTesting
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.leinardi.forlago.library.android.api.coroutine.CoroutineDispatchers
import com.leinardi.forlago.library.android.api.interactor.android.CopyToClipboardInteractor
import com.leinardi.forlago.library.android.api.interactor.android.DeleteWebViewDataInteractor
import com.leinardi.forlago.library.android.api.interactor.android.GetAppUpdateInfoInteractor
import com.leinardi.forlago.library.android.api.interactor.android.GetAppVersionNameInteractor
import com.leinardi.forlago.library.android.api.interactor.android.GetConnectivityInteractor
import com.leinardi.forlago.library.android.api.interactor.android.GetDefaultLanguageCodeInteractor
import com.leinardi.forlago.library.android.api.interactor.android.GetInstallStateUpdateStreamInteractor
import com.leinardi.forlago.library.android.api.interactor.android.OpenUrlInWebBrowserInteractor
import com.leinardi.forlago.library.android.api.interactor.android.RestartApplicationInteractor
import com.leinardi.forlago.library.android.api.interactor.android.ShareUrlInteractor
import com.leinardi.forlago.library.android.api.interactor.android.ShowToastInteractor
import com.leinardi.forlago.library.android.api.interactor.encryption.DecryptDeterministicallyInteractor
import com.leinardi.forlago.library.android.api.interactor.encryption.DecryptInteractor
import com.leinardi.forlago.library.android.api.interactor.encryption.EncryptDeterministicallyInteractor
import com.leinardi.forlago.library.android.api.interactor.encryption.EncryptInteractor
import com.leinardi.forlago.library.android.encryption.CryptoHelper
import com.leinardi.forlago.library.android.interactor.android.CopyToClipboardInteractorImpl
import com.leinardi.forlago.library.android.interactor.android.DeleteWebViewDataInteractorImpl
import com.leinardi.forlago.library.android.interactor.android.GetAppUpdateInfoInteractorImpl
import com.leinardi.forlago.library.android.interactor.android.GetAppVersionNameInteractorImpl
import com.leinardi.forlago.library.android.interactor.android.GetConnectivityInteractorImpl
import com.leinardi.forlago.library.android.interactor.android.GetDefaultLanguageCodeInteractorImpl
import com.leinardi.forlago.library.android.interactor.android.GetInstallStateUpdateStreamInteractorImpl
import com.leinardi.forlago.library.android.interactor.android.OpenUrlInWebBrowserInteractorImpl
import com.leinardi.forlago.library.android.interactor.android.RestartApplicationInteractorImpl
import com.leinardi.forlago.library.android.interactor.android.ShareUrlInteractorImpl
import com.leinardi.forlago.library.android.interactor.android.ShowToastInteractorImpl
import com.leinardi.forlago.library.android.interactor.encryption.DecryptDeterministicallyInteractorImpl
import com.leinardi.forlago.library.android.interactor.encryption.DecryptInteractorImpl
import com.leinardi.forlago.library.android.interactor.encryption.EncryptDeterministicallyInteractorImpl
import com.leinardi.forlago.library.android.interactor.encryption.EncryptInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Module(includes = [AndroidModule.BindModule::class])
@InstallIn(SingletonComponent::class)
open class AndroidModule {
    @Provides
    @Singleton
    fun provideAccountManager(application: Application): AccountManager = AccountManager.get(application)

    @Provides
    @Singleton
    fun provideAppUpdateManager(application: Application): AppUpdateManager = getAppUpdateManager(application)

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

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    protected open fun getAppUpdateManager(
        application: Application,
    ): AppUpdateManager = AppUpdateManagerFactory.create(application)

    @Module
    @InstallIn(SingletonComponent::class)
    internal interface BindModule {
        // Android Interactors
        @Binds
        fun bindCopyToClipboardInteractor(bind: CopyToClipboardInteractorImpl): CopyToClipboardInteractor

        @Binds
        fun bindDeleteWebViewDataInteractor(bind: DeleteWebViewDataInteractorImpl): DeleteWebViewDataInteractor

        @Binds
        fun bindGetAppUpdateInfoInteractor(bind: GetAppUpdateInfoInteractorImpl): GetAppUpdateInfoInteractor

        @Binds
        fun bindGetAppVersionNameInteractor(bind: GetAppVersionNameInteractorImpl): GetAppVersionNameInteractor

        @Binds
        fun bindGetConnectivityInteractor(bind: GetConnectivityInteractorImpl): GetConnectivityInteractor

        @Binds
        fun bindGetDefaultLanguageCodeInteractor(bind: GetDefaultLanguageCodeInteractorImpl): GetDefaultLanguageCodeInteractor

        @Binds
        fun bindGetInstallStateUpdateInteractor(bind: GetInstallStateUpdateStreamInteractorImpl): GetInstallStateUpdateStreamInteractor

        @Binds
        fun bindOpenUrlInWebBrowserInteractor(bind: OpenUrlInWebBrowserInteractorImpl): OpenUrlInWebBrowserInteractor

        @Binds
        fun bindRestartApplicationInteractor(bind: RestartApplicationInteractorImpl): RestartApplicationInteractor

        @Binds
        fun bindShareUrlInteractor(bind: ShareUrlInteractorImpl): ShareUrlInteractor

        @Binds
        fun bindShowToastInteractor(bind: ShowToastInteractorImpl): ShowToastInteractor

        // Encryption interactors
        @Binds
        fun bindDecryptDeterministicallyInteractor(bind: DecryptDeterministicallyInteractorImpl): DecryptDeterministicallyInteractor

        @Binds
        fun bindDecryptInteractor(bind: DecryptInteractorImpl): DecryptInteractor

        @Binds
        fun bindEncryptDeterministicallyInteractor(bind: EncryptDeterministicallyInteractorImpl): EncryptDeterministicallyInteractor

        @Binds
        fun bindEncryptInteractor(bind: EncryptInteractorImpl): EncryptInteractor
    }
}
