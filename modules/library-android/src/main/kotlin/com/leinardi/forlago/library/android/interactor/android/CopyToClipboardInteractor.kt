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

package com.leinardi.forlago.library.android.interactor.android

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.leinardi.forlago.library.android.coroutine.CoroutineDispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CopyToClipboardInteractor @Inject constructor(
    private val application: Application,
    private val dispatchers: CoroutineDispatchers,
) {
    private val clipboardManager by lazy { application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }

    suspend operator fun invoke(text: String, description: String = "") {
        withContext(dispatchers.io) {
            val clipData = ClipData.newPlainText(description, text)
            Timber.d("Copying ${clipData.getItemAt(0).text} to clipboard")
            clipboardManager.setPrimaryClip(clipData)
        }
    }
}
