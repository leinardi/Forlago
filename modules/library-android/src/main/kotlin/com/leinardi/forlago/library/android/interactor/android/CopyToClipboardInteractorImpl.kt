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
import android.content.ClipData
import android.content.ClipboardManager
import androidx.core.content.ContextCompat
import com.leinardi.forlago.library.android.api.coroutine.CoroutineDispatchers
import com.leinardi.forlago.library.android.api.interactor.android.CopyToClipboardInteractor
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CopyToClipboardInteractorImpl @Inject constructor(
    private val application: Application,
    private val dispatchers: CoroutineDispatchers,
) : CopyToClipboardInteractor {
    private val clipboardManager by lazy { ContextCompat.getSystemService(application, ClipboardManager::class.java) }

    override suspend operator fun invoke(text: String, description: String) {
        withContext(dispatchers.io) {
            val clipData = ClipData.newPlainText(description, text)
            clipboardManager?.let { clipboardManager ->
                Timber.d("Copying ${clipData.getItemAt(0).text} to clipboard")
                clipboardManager.setPrimaryClip(clipData)
            }
        }
    }
}
