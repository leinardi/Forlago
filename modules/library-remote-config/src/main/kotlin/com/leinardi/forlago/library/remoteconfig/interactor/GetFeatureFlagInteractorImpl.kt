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

package com.leinardi.forlago.library.remoteconfig.interactor

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.leinardi.forlago.library.annotation.AutoBind
import com.leinardi.forlago.library.remoteconfig.api.interactor.GetFeatureFlagInteractor
import com.leinardi.forlago.library.remoteconfig.api.model.RemoteConfigValue
import com.leinardi.forlago.library.remoteconfig.getRemoteConfigDefaults
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@AutoBind
@Singleton
internal class GetFeatureFlagInteractorImpl @Inject constructor(
    application: Application,
) : GetFeatureFlagInteractor {
    private val isFirebaseInitialized = FirebaseApp.getApps(application).isNotEmpty()
    private val remoteConfigDefaults = getRemoteConfigDefaults(application)
    override fun getBoolean(key: String): RemoteConfigValue.Boolean {
        var boolean: Boolean = FirebaseRemoteConfig.DEFAULT_VALUE_FOR_BOOLEAN
        var valueSource = RemoteConfigValue.ValueSource.STATIC
        var lastFetchStatus: RemoteConfigValue.LastFetchStatus? = null
        if (isFirebaseInitialized) {
            getValue(key).run {
                boolean = asBoolean()
                valueSource = getValueSource()
            }
            lastFetchStatus = getLastFetchStatus()
        } else {
            remoteConfigDefaults[key]?.toBooleanStrictOrNull()?.run {
                boolean = this
                valueSource = RemoteConfigValue.ValueSource.DEFAULT
            }
        }
        return RemoteConfigValue.Boolean(
            value = boolean,
            valueSource = valueSource,
            lastFetchStatus = lastFetchStatus,
        ).also { Timber.d("Getting remote value \"$key\": $it") }
    }

    override fun getByteArray(key: String): RemoteConfigValue.ByteArray {
        var byteArray: ByteArray = FirebaseRemoteConfig.DEFAULT_VALUE_FOR_BYTE_ARRAY
        var valueSource = RemoteConfigValue.ValueSource.STATIC
        var lastFetchStatus: RemoteConfigValue.LastFetchStatus? = null
        if (isFirebaseInitialized) {
            getValue(key).run {
                byteArray = asByteArray()
                valueSource = getValueSource()
            }
            lastFetchStatus = getLastFetchStatus()
        } else {
            remoteConfigDefaults[key]?.run { toByteArray().let { byteArray = it }.also { valueSource = RemoteConfigValue.ValueSource.DEFAULT } }
        }
        return RemoteConfigValue.ByteArray(
            value = byteArray,
            valueSource = valueSource,
            lastFetchStatus = lastFetchStatus,
        ).also { Timber.d("Getting remote value \"$key\": $it") }
    }

    override fun getDouble(key: String): RemoteConfigValue.Double {
        var double: Double = FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE
        var valueSource = RemoteConfigValue.ValueSource.STATIC
        var lastFetchStatus: RemoteConfigValue.LastFetchStatus? = null
        if (isFirebaseInitialized) {
            getValue(key).run {
                double = asDouble()
                valueSource = getValueSource()
            }
            lastFetchStatus = getLastFetchStatus()
        } else {
            remoteConfigDefaults[key]?.toDoubleOrNull()?.run {
                double = this
                valueSource = RemoteConfigValue.ValueSource.DEFAULT
            }
        }
        return RemoteConfigValue.Double(
            value = double,
            valueSource = valueSource,
            lastFetchStatus = lastFetchStatus,
        ).also { Timber.d("Getting remote value \"$key\": $it") }
    }

    override fun getLong(key: String): RemoteConfigValue.Long {
        var long: Long = FirebaseRemoteConfig.DEFAULT_VALUE_FOR_LONG
        var valueSource = RemoteConfigValue.ValueSource.STATIC
        var lastFetchStatus: RemoteConfigValue.LastFetchStatus? = null
        if (isFirebaseInitialized) {
            getValue(key).run {
                long = asLong()
                valueSource = getValueSource()
            }
            lastFetchStatus = getLastFetchStatus()
        } else {
            remoteConfigDefaults[key]?.toLongOrNull()?.run {
                long = this
                valueSource = RemoteConfigValue.ValueSource.DEFAULT
            }
        }
        return RemoteConfigValue.Long(
            value = long,
            valueSource = valueSource,
            lastFetchStatus = lastFetchStatus,
        ).also { Timber.d("Getting remote value \"$key\": $it") }
    }

    override fun getString(key: String): RemoteConfigValue.String {
        var string: String = FirebaseRemoteConfig.DEFAULT_VALUE_FOR_STRING
        var valueSource = RemoteConfigValue.ValueSource.STATIC
        var lastFetchStatus: RemoteConfigValue.LastFetchStatus? = null
        if (isFirebaseInitialized) {
            getValue(key).run {
                string = asString()
                valueSource = getValueSource()
            }
            lastFetchStatus = getLastFetchStatus()
        } else {
            remoteConfigDefaults[key]?.run {
                string = this
                valueSource = RemoteConfigValue.ValueSource.DEFAULT
            }
        }
        return RemoteConfigValue.String(
            value = string,
            valueSource = valueSource,
            lastFetchStatus = lastFetchStatus,
        ).also { Timber.d("Getting remote value \"$key\": $it") }
    }

    private fun getValue(key: String): FirebaseRemoteConfigValue = FirebaseRemoteConfig.getInstance().getValue(key)

    private fun FirebaseRemoteConfigValue.getValueSource(): RemoteConfigValue.ValueSource =
        when (source) {
            FirebaseRemoteConfig.VALUE_SOURCE_DEFAULT -> RemoteConfigValue.ValueSource.DEFAULT
            FirebaseRemoteConfig.VALUE_SOURCE_REMOTE -> RemoteConfigValue.ValueSource.REMOTE
            FirebaseRemoteConfig.VALUE_SOURCE_STATIC -> RemoteConfigValue.ValueSource.STATIC
            else -> error("Unknown value source: $source")
        }

    private fun getLastFetchStatus(): RemoteConfigValue.LastFetchStatus =
        when (val status = FirebaseRemoteConfig.getInstance().info.lastFetchStatus) {
            FirebaseRemoteConfig.LAST_FETCH_STATUS_FAILURE -> RemoteConfigValue.LastFetchStatus.FAILURE
            FirebaseRemoteConfig.LAST_FETCH_STATUS_NO_FETCH_YET -> RemoteConfigValue.LastFetchStatus.NO_FETCH_YET
            FirebaseRemoteConfig.LAST_FETCH_STATUS_SUCCESS -> RemoteConfigValue.LastFetchStatus.SUCCESS
            FirebaseRemoteConfig.LAST_FETCH_STATUS_THROTTLED -> RemoteConfigValue.LastFetchStatus.THROTTLED
            else -> error("Unknown l ast fetch status: $status")
        }
}
