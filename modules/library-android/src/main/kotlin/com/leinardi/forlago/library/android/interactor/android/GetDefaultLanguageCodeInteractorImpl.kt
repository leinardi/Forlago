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

package com.leinardi.forlago.library.android.interactor.android

import android.app.Application
import com.leinardi.forlago.library.android.api.interactor.android.GetDefaultLanguageCodeInteractor
import com.leinardi.forlago.library.annotation.AutoBind
import javax.inject.Inject

@AutoBind
internal class GetDefaultLanguageCodeInteractorImpl @Inject constructor(
    private val application: Application,
) : GetDefaultLanguageCodeInteractor {
    override operator fun invoke(): String? = application.resources.configuration.locales.get(0).language
}
