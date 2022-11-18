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

import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.FormatStyle

fun String.urlEncode(): String = URLEncoder.encode(this, StandardCharsets.UTF_8.name())

fun String.urlDecode(): String = URLDecoder.decode(this, StandardCharsets.UTF_8.name())

fun String.toLocalizedDate(dateStyle: FormatStyle = FormatStyle.LONG): String = LocalDateTime.parse(this).toLocalizedDate(dateStyle)

fun String.fromDateToLocalizedDate(dateStyle: FormatStyle = FormatStyle.LONG): String = LocalDate.parse(this).toLocalizedDate(dateStyle)
