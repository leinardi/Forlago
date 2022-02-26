/*
 * Copyright 2021 Roberto Leinardi.
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

package com.leinardi.forlago.core.preferences.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

class DataStoreRepository constructor(
    private val context: Context,
    private val preferenceName: String,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = preferenceName)

    suspend fun <T> storeValue(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit {
            it[key] = value
        }
    }

    suspend fun <T> removeKey(key: Preferences.Key<T>) {
        context.dataStore.edit {
            it.remove(key)
        }
    }

    suspend fun <T> readValue(key: Preferences.Key<T>): T? =
        context.dataStore.getFromLocalStorage(key).firstOrNull()

    suspend fun <T> readValue(key: Preferences.Key<T>, defaultValue: T): T =
        context.dataStore.getFromLocalStorage(key).map { it ?: defaultValue }.first()

    suspend fun clearPreferencesStorage() {
        Timber.d("Clear $preferenceName data store")
        context.dataStore.edit {
            it.clear()
        }
    }

    private fun <T> DataStore<Preferences>.getFromLocalStorage(
        preferencesKey: Preferences.Key<T>,
    ): Flow<T?> =
        data.catch {
            if (it is IOException) {
                Timber.e(it, "Error while reading data store.")
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[preferencesKey]
        }
}
