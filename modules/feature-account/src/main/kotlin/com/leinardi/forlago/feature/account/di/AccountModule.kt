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

package com.leinardi.forlago.feature.account.di

import com.leinardi.forlago.feature.account.api.interactor.account.AddAccountInteractor
import com.leinardi.forlago.feature.account.api.interactor.account.GetAccountInteractor
import com.leinardi.forlago.feature.account.api.interactor.account.RemoveAccountsInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.GetAccessTokenExpiryInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.GetAccessTokenInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.GetJwtExpiresAtInMillisInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.GetRefreshTokenInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.InvalidateAccessTokenInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.InvalidateRefreshTokenInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.IsJwtExpiredInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.PeekAccessTokenInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.RefreshAccessTokenInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.SetRefreshTokenInteractor
import com.leinardi.forlago.feature.account.interactor.account.AddAccountInteractorImpl
import com.leinardi.forlago.feature.account.interactor.account.GetAccountInteractorImpl
import com.leinardi.forlago.feature.account.interactor.account.RemoveAccountsInteractorImpl
import com.leinardi.forlago.feature.account.interactor.token.GetAccessTokenExpiryInteractorImpl
import com.leinardi.forlago.feature.account.interactor.token.GetAccessTokenInteractorImpl
import com.leinardi.forlago.feature.account.interactor.token.GetJwtExpiresAtInMillisInteractorImpl
import com.leinardi.forlago.feature.account.interactor.token.GetRefreshTokenInteractorImpl
import com.leinardi.forlago.feature.account.interactor.token.InvalidateAccessTokenInteractorImpl
import com.leinardi.forlago.feature.account.interactor.token.InvalidateRefreshTokenInteractorImpl
import com.leinardi.forlago.feature.account.interactor.token.IsJwtExpiredInteractorImpl
import com.leinardi.forlago.feature.account.interactor.token.PeekAccessTokenInteractorImpl
import com.leinardi.forlago.feature.account.interactor.token.RefreshAccessTokenInteractorImpl
import com.leinardi.forlago.feature.account.interactor.token.SetRefreshTokenInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface AccountModule {
    // Account interactors
    @Binds
    fun bindAddAccountInteractor(bind: AddAccountInteractorImpl): AddAccountInteractor

    @Binds
    fun bindGetAccountInteractor(bind: GetAccountInteractorImpl): GetAccountInteractor

    @Binds
    fun bindRemoveAccountsInteractor(bind: RemoveAccountsInteractorImpl): RemoveAccountsInteractor

    // Token interactors
    @Binds
    fun bindGetAccessTokenExpiryInteractor(bind: GetAccessTokenExpiryInteractorImpl): GetAccessTokenExpiryInteractor

    @Binds
    fun bindGetAccessTokenInteractor(bind: GetAccessTokenInteractorImpl): GetAccessTokenInteractor

    @Binds
    fun bindGetJwtExpiresAtInMillisInteractor(bind: GetJwtExpiresAtInMillisInteractorImpl): GetJwtExpiresAtInMillisInteractor

    @Binds
    fun bindGetRefreshTokenInteractor(bind: GetRefreshTokenInteractorImpl): GetRefreshTokenInteractor

    @Binds
    fun bindInvalidateAccessTokenInteractor(bind: InvalidateAccessTokenInteractorImpl): InvalidateAccessTokenInteractor

    @Binds
    fun bindInvalidateRefreshTokenInteractor(bind: InvalidateRefreshTokenInteractorImpl): InvalidateRefreshTokenInteractor

    @Binds
    fun bindIsJwtExpiredInteractor(bind: IsJwtExpiredInteractorImpl): IsJwtExpiredInteractor

    @Binds
    fun bindPeekAccessTokenInteractor(bind: PeekAccessTokenInteractorImpl): PeekAccessTokenInteractor

    @Binds
    fun bindRefreshAccessTokenInteractor(bind: RefreshAccessTokenInteractorImpl): RefreshAccessTokenInteractor

    @Binds
    fun bindSetRefreshTokenInteractor(bind: SetRefreshTokenInteractorImpl): SetRefreshTokenInteractor
}
