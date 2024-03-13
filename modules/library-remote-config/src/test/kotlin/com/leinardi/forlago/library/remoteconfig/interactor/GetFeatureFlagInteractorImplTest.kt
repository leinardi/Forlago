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
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.leinardi.forlago.library.remoteconfig.api.model.RemoteConfigValue
import com.leinardi.forlago.library.remoteconfig.getRemoteConfigDefaults
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])  // With Robolectric 4.10.3 maxSdkVersion supported = 33
class GetFeatureFlagInteractorImplTest {
    private lateinit var getFeatureFlagInteractorImpl: GetFeatureFlagInteractorImpl
    private lateinit var remoteConfigDefaults: Map<String, String>

    @Before
    fun setUp() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        remoteConfigDefaults = getRemoteConfigDefaults(application)
        getFeatureFlagInteractorImpl = GetFeatureFlagInteractorImpl(application)
    }

    @Test
    fun `GIVEN no Firebase initialization WHEN getLong is called with a valid key THEN returns default value`(): TestResult = runTest {
        // Given
        // no Firebase initialization

        // When
        val key = "testLong"
        val result = getFeatureFlagInteractorImpl.getLong(key)

        // Then
        assertEquals(remoteConfigDefaults[key]?.toLongOrNull(), result.value)
        assertEquals(RemoteConfigValue.ValueSource.DEFAULT, result.valueSource)
    }

    @Test
    fun `GIVEN no Firebase initialization WHEN getLong is called with an invalid key THEN returns static value`(): TestResult = runTest {
        // Given
        // no Firebase initialization

        // When
        val key = "someInvalidKey"
        val result = getFeatureFlagInteractorImpl.getLong(key)

        // Then
        assertEquals(FirebaseRemoteConfig.DEFAULT_VALUE_FOR_LONG, result.value)
        assertEquals(RemoteConfigValue.ValueSource.STATIC, result.valueSource)
    }

    @Test
    fun `GIVEN no Firebase initialization WHEN getDouble is called with a valid key THEN returns default value`(): TestResult = runTest {
        // Given
        // no Firebase initialization

        // When
        val key = "testDouble"
        val result = getFeatureFlagInteractorImpl.getDouble(key)

        // Then
        assertEquals(remoteConfigDefaults[key]?.toDoubleOrNull(), result.value)
        assertEquals(RemoteConfigValue.ValueSource.DEFAULT, result.valueSource)
    }

    @Test
    fun `GIVEN no Firebase initialization WHEN getDouble is called with an invalid key THEN returns static value`(): TestResult = runTest {
        // Given
        // no Firebase initialization

        // When
        val key = "someInvalidKey"
        val result = getFeatureFlagInteractorImpl.getDouble(key)

        // Then
        assertEquals(FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE, result.value)
        assertEquals(RemoteConfigValue.ValueSource.STATIC, result.valueSource)
    }

    @Test
    fun `GIVEN no Firebase initialization WHEN getString is called with a valid key THEN returns default value`(): TestResult = runTest {
        // Given
        // no Firebase initialization

        // When
        val key = "testString"
        val result = getFeatureFlagInteractorImpl.getString(key)

        // Then
        assertEquals(remoteConfigDefaults[key], result.value)
        assertEquals(RemoteConfigValue.ValueSource.DEFAULT, result.valueSource)
    }

    @Test
    fun `GIVEN no Firebase initialization WHEN getString is called with an invalid key THEN returns static value`(): TestResult = runTest {
        // Given
        // no Firebase initialization

        // When
        val key = "someInvalidKey"
        val result = getFeatureFlagInteractorImpl.getString(key)

        // Then
        assertEquals(FirebaseRemoteConfig.DEFAULT_VALUE_FOR_STRING, result.value)
        assertEquals(RemoteConfigValue.ValueSource.STATIC, result.valueSource)
    }

    @Test
    fun `GIVEN no Firebase initialization WHEN getByteArray is called with an invalid key THEN returns static value`(): TestResult = runTest {
        // Given
        // no Firebase initialization

        // When
        val key = "someInvalidKey"
        val result = getFeatureFlagInteractorImpl.getByteArray(key)

        // Then
        assertEquals(FirebaseRemoteConfig.DEFAULT_VALUE_FOR_BYTE_ARRAY, result.value)
        assertEquals(RemoteConfigValue.ValueSource.STATIC, result.valueSource)
    }

    @Test
    fun `GIVEN no Firebase initialization WHEN getBoolean is called with a valid key THEN returns default value`(): TestResult = runTest {
        // Given
        // no Firebase initialization

        // When
        val key = "testBoolean"
        val result = getFeatureFlagInteractorImpl.getBoolean(key)

        // Then
        assertEquals(remoteConfigDefaults[key]?.toBooleanStrictOrNull(), result.value)
        assertEquals(RemoteConfigValue.ValueSource.DEFAULT, result.valueSource)
    }

    @Test
    fun `GIVEN no Firebase initialization WHEN getBoolean is called with an invalid key THEN returns static value`(): TestResult = runTest {
        // Given
        // no Firebase initialization

        // When
        val key = "someInvalidKey"
        val result = getFeatureFlagInteractorImpl.getBoolean(key)

        // Then
        assertEquals(FirebaseRemoteConfig.DEFAULT_VALUE_FOR_BOOLEAN, result.value)
        assertEquals(RemoteConfigValue.ValueSource.STATIC, result.valueSource)
    }
}
