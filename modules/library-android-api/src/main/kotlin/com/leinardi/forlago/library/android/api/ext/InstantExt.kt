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

package com.leinardi.forlago.library.android.api.ext

import android.content.Context
import android.text.format.DateUtils
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

fun Instant.toLocalizedDateTime(dateTimeStyle: FormatStyle = FormatStyle.MEDIUM): String =
    atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofLocalizedDateTime(dateTimeStyle))

/**
 * Format a date / time such that if the [Instant] is on the same day as now, it shows just the time, if it's on the same year as now, it shows just
 * the day and abbreviated month (e.g. Oct 9) and in any other case, it shows just the date (e.g. 12/31/2007).
 *
 * @return the formatted date / time
 */
fun Instant.toLocalizedSmartDateTime(context: Context): String {
    val now = Instant.now()
    return when {
        truncatedTo(ChronoUnit.DAYS).equals(now.truncatedTo(ChronoUnit.DAYS)) ->
            DateUtils.formatDateTime(context, toEpochMilli(), DateUtils.FORMAT_SHOW_TIME)

        atZone(ZoneId.systemDefault()).year == now.atZone(ZoneId.systemDefault()).year ->
            DateUtils.formatDateTime(context, toEpochMilli(), DateUtils.FORMAT_ABBREV_MONTH or DateUtils.FORMAT_NO_YEAR)

        else ->
            DateUtils.formatDateTime(context, toEpochMilli(), DateUtils.FORMAT_NUMERIC_DATE)
    }
}

fun Instant.toLocalizedDate(dateStyle: FormatStyle = FormatStyle.LONG): String =
    atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofLocalizedDate(dateStyle))
