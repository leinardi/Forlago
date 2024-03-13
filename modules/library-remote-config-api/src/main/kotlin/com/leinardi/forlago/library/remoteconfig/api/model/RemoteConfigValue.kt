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

package com.leinardi.forlago.library.remoteconfig.api.model

sealed class RemoteConfigValue {
    abstract val valueSource: ValueSource
    abstract val lastFetchStatus: LastFetchStatus?

    data class Long(
        val value: kotlin.Long,
        override val valueSource: ValueSource,
        override val lastFetchStatus: LastFetchStatus?,
    ) : RemoteConfigValue()

    data class Double(
        val value: kotlin.Double,
        override val valueSource: ValueSource,
        override val lastFetchStatus: LastFetchStatus?,
    ) : RemoteConfigValue()

    data class String(
        val value: kotlin.String,
        override val valueSource: ValueSource,
        override val lastFetchStatus: LastFetchStatus?,
    ) : RemoteConfigValue()

    data class ByteArray(
        val value: kotlin.ByteArray,
        override val valueSource: ValueSource,
        override val lastFetchStatus: LastFetchStatus?,
    ) : RemoteConfigValue() {
        override fun equals(other: Any?): kotlin.Boolean {
            if (this === other) {
                return true
            }
            if (javaClass != other?.javaClass) {
                return false
            }

            other as ByteArray

            if (!value.contentEquals(other.value)) {
                return false
            }
            if (valueSource != other.valueSource) {
                return false
            }
            return lastFetchStatus == other.lastFetchStatus
        }

        @Suppress("MagicNumber")
        override fun hashCode(): Int {
            var result = value.contentHashCode()
            result = 31 * result + valueSource.hashCode()
            result = 31 * result + lastFetchStatus.hashCode()
            return result
        }
    }

    data class Boolean(
        val value: kotlin.Boolean,
        override val valueSource: ValueSource,
        override val lastFetchStatus: LastFetchStatus?,
    ) : RemoteConfigValue()

    enum class ValueSource {
        DEFAULT,
        REMOTE,
        STATIC,
    }

    enum class LastFetchStatus {
        FAILURE,
        NO_FETCH_YET,
        SUCCESS,
        THROTTLED
    }
}
