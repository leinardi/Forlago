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

package com.leinardi.forlago.library.network.interactor

import com.leinardi.forlago.library.network.di.NetworkModule
import com.leinardi.forlago.library.network.interactor.account.GetJwtExpiresAtInMillisInteractor
import com.leinardi.forlago.library.network.interactor.account.IsJwtExpiredInteractor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.time.Clock
import java.util.concurrent.TimeUnit
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsJwtExpiredInteractorTest {
    private val clock = mockk<Clock>()
    private val getJwtExpiresAtInteractor = GetJwtExpiresAtInMillisInteractor(NetworkModule.provideJson())
    private val isJwtExpiredInteractor = IsJwtExpiredInteractor(clock, getJwtExpiresAtInteractor)

    @Test
    fun `GIVEN current time is before the expires at WHEN call isJwtExpiredInteractor THEN return false`() {
        // Given
        every { clock.millis() } returns TimeUnit.SECONDS.toMillis(EXPIRES_AT) - 1

        // When
        val isExpired = isJwtExpiredInteractor(VALID_JWT)

        // Then
        assertFalse(isExpired)
    }

    @Test
    fun `GIVEN current time is equal the expires at WHEN call isJwtExpiredInteractor THEN return true`() {
        // Given
        every { clock.millis() } returns TimeUnit.SECONDS.toMillis(EXPIRES_AT)

        // When
        val isExpired = isJwtExpiredInteractor(VALID_JWT)

        // Then
        assertTrue(isExpired)
    }

    @Test
    fun `GIVEN current time is after the expires at WHEN call isJwtExpiredInteractor THEN return true`() {
        // Given
        every { clock.millis() } returns TimeUnit.SECONDS.toMillis(EXPIRES_AT) + 1

        // When
        val isExpired = isJwtExpiredInteractor(VALID_JWT)

        // Then
        assertTrue(isExpired)
    }

    companion object {
        private const val EXPIRES_AT = 1_643_025_283L
        private const val VALID_JWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjQzMDI1Mjgz" +
            "LCJvcmlnX2lhdCI6IjdmYTJjNDg3NjNiYTQwM2RhOWJhZWIxN2Y5M2JlNWQ2IiwidXNlcl9pZCI6MjA4Mn0.TMf-gUxhmnbm_MkDozvSoT60TuAwPtd4fHmj4YIcjMY"
    }
}
