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

package com.leinardi.forlago.feature.login.interactor

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.unwrap
import com.leinardi.forlago.library.feature.interactor.GetFeaturesInteractor
import com.leinardi.forlago.library.network.api.model.AuthErrResult
import com.leinardi.forlago.library.test.coroutine.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class LogInInteractorImplTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()
    private val getFeaturesInteractor: GetFeaturesInteractor = mockk()
    private val signInInteractor = LogInInteractorImpl(getFeaturesInteractor)

    @Test
    fun `GIVEN remote success WHEN call logInInteractor with password THEN return Success `(): TestResult = runTest {
        // Given
        every { getFeaturesInteractor() } returns emptyList()
        val usernameExpected = "user"

        // When
        val result = signInInteractor.invoke(username = usernameExpected, password = "pass", success = true)

        // Then
        verify(exactly = 1) { getFeaturesInteractor() }
        assertEquals(result.unwrap().username, usernameExpected)
    }

    @Test
    fun `GIVEN remote error WHEN call logInInteractor with password THEN return Error `(): TestResult = runTest {
        // Given
        every { getFeaturesInteractor() } returns emptyList()
        val usernameExpected = "user"

        // When
        val result = signInInteractor.invoke(username = usernameExpected, password = "pass", success = false)

        // Then
        assertEquals(Err(AuthErrResult.BadAuthentication(403)), result)
    }
}
