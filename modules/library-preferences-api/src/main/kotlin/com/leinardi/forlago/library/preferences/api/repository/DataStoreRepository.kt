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

package com.leinardi.forlago.library.preferences.api.repository

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun clearPreferencesStorage()

    fun <T> observeValue(key: Preferences.Key<T>): Flow<T?>

    suspend fun <T> readValue(key: Preferences.Key<T>): T?

    suspend fun <T> readValue(key: Preferences.Key<T>, defaultValue: T): T

    suspend fun <T> removeKey(key: Preferences.Key<T>)

    suspend fun <T> storeValue(key: Preferences.Key<T>, value: T)
}
