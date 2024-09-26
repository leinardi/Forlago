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

import android.webkit.CookieManager
import android.webkit.WebStorage
import com.leinardi.forlago.library.android.api.coroutine.CoroutineDispatchers
import com.leinardi.forlago.library.android.api.interactor.android.DeleteWebViewDataInteractor
import com.leinardi.forlago.library.annotation.AutoBind
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AutoBind
internal class DeleteWebViewDataInteractorImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
) : DeleteWebViewDataInteractor {
    override suspend operator fun invoke() {
        withContext(dispatchers.io) {
            // Clear all the Application Cache, Web SQL Database and the HTML5 Web Storage
            WebStorage.getInstance().deleteAllData()
            // Clear all the cookies
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
        }
    }
}
