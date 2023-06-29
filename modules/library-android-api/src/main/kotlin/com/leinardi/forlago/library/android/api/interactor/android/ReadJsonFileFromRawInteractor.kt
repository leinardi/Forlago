/*
 * Copyright 2023 Roberto Leinardi.
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

package com.leinardi.forlago.library.android.api.interactor.android

import android.app.Application
import androidx.annotation.RawRes
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class ReadJsonFileFromRawInteractor @Inject constructor(
    val application: Application,
    val json: Json,
) {
    inline operator fun <reified T> invoke(@RawRes rawId: Int): T? = try {
        val jsonString = application.resources.openRawResource(rawId).bufferedReader().use { it.readText() }
        json.decodeFromString<T>(jsonString)
    } catch (ioException: IOException) {
        Timber.e(ioException)
        null
    }
}
