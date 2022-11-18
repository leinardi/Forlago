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

package com.leinardi.forlago.library.android.api.room

import androidx.room.TypeConverter
import java.time.Instant
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object LinkTypeConverters {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?) = value?.let { formatter.parse(value, OffsetDateTime::from) }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? = date?.format(formatter)

    @TypeConverter
    @JvmStatic
    fun toInstant(value: Long?) = value?.let { Instant.ofEpochMilli(it) }

    @TypeConverter
    @JvmStatic
    fun fromInstant(date: Instant?) = date?.toEpochMilli()
}
