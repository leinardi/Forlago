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

package com.leinardi.forlago.library.android.api.ext

import android.app.Activity
import android.content.Intent
import androidx.core.net.toUri

fun Activity.sendEmail(
    address: String? = null,
    subject: String? = null,
    body: String? = null,
) {
    val selectorIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:${address.orEmpty()}".toUri()
    }
    val emailIntent = Intent(Intent.ACTION_SEND).apply {
        address?.let { putExtra(Intent.EXTRA_EMAIL, arrayOf(it)) }
        subject?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
        body?.let { putExtra(Intent.EXTRA_TEXT, it) }
        selector = selectorIntent
    }
    startActivity(Intent.createChooser(emailIntent, null))
}
