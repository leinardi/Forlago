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

package com.leinardi.forlago.library.ui.di

import com.leinardi.forlago.library.ui.api.interactor.GetMaterialYouFlowInteractor
import com.leinardi.forlago.library.ui.api.interactor.GetThemeFlowInteractor
import com.leinardi.forlago.library.ui.api.interactor.SetNightModeInteractor
import com.leinardi.forlago.library.ui.api.interactor.StoreMaterialYouInteractor
import com.leinardi.forlago.library.ui.api.interactor.StoreThemeInteractor
import com.leinardi.forlago.library.ui.interactor.GetMaterialYouFlowInteractorImpl
import com.leinardi.forlago.library.ui.interactor.GetThemeFlowInteractorImpl
import com.leinardi.forlago.library.ui.interactor.SetNightModeInteractorImpl
import com.leinardi.forlago.library.ui.interactor.StoreMaterialYouInteractorImpl
import com.leinardi.forlago.library.ui.interactor.StoreThemeInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface UiModule {
    @Binds
    fun bindGetMaterialYouFlowInteractor(bind: GetMaterialYouFlowInteractorImpl): GetMaterialYouFlowInteractor

    @Binds
    fun bindGetThemeFlowInteractor(bind: GetThemeFlowInteractorImpl): GetThemeFlowInteractor

    @Binds
    fun bindSetNightModeInteractor(bind: SetNightModeInteractorImpl): SetNightModeInteractor

    @Binds
    fun bindStoreMaterialYouInteractor(bind: StoreMaterialYouInteractorImpl): StoreMaterialYouInteractor

    @Binds
    fun bindStoreThemeInteractor(bind: StoreThemeInteractorImpl): StoreThemeInteractor
}
