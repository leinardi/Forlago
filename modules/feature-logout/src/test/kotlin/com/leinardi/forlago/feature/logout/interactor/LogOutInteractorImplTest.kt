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

package com.leinardi.forlago.feature.logout.interactor

import android.os.Build
import com.leinardi.forlago.feature.login.api.interactor.IsLogInInProgressInteractor
import com.leinardi.forlago.feature.logout.api.interactor.LogOutInteractor
import com.leinardi.forlago.library.feature.interactor.GetFeaturesInteractor
import com.leinardi.forlago.library.navigation.api.navigator.ForlagoNavigator
import com.leinardi.forlago.library.network.api.interactor.ClearApolloCacheInteractor
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.VANILLA_ICE_CREAM])  // Robolectric 4.14 adds support for Android V (SDK 35)
class LogOutInteractorImplTest {
    private val clearApolloCacheInteractor: ClearApolloCacheInteractor = mockk()
    private val forlagoNavigator: ForlagoNavigator = mockk()
    private val getFeaturesInteractor: GetFeaturesInteractor = mockk()
    private val isLogInInProgressInteractor: IsLogInInProgressInteractor = mockk()
    private lateinit var logOutInteractor: LogOutInteractor

    @Before
    fun setUp() {
        logOutInteractor = LogOutInteractorImpl(
            clearApolloCacheInteractor,
            forlagoNavigator,
            getFeaturesInteractor,
            isLogInInProgressInteractor,
        )
        every { isLogInInProgressInteractor.invoke() } returns false
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `GIVEN navigateToLogin false WHEN call logOutInteractor THEN clear data & no navigate`(): TestResult = runTest {
        // Given
        every { getFeaturesInteractor() } returns emptyList()
        coEvery { clearApolloCacheInteractor() } returns true
        coEvery { forlagoNavigator.navigate(any()) } returns true

        // When
        logOutInteractor(false)

        // Then
        verify(exactly = 1) { getFeaturesInteractor() }
        coVerify(exactly = 1) { clearApolloCacheInteractor() }
        coVerify(exactly = 0) { forlagoNavigator.navigate(any()) }
    }
}
