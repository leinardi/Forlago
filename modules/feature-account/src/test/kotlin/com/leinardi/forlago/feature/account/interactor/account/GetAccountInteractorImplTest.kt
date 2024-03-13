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

package com.leinardi.forlago.feature.account.interactor.account

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Build
import com.leinardi.forlago.feature.account.AccountAuthenticatorConfig
import com.leinardi.forlago.feature.account.api.interactor.account.GetAccountInteractor
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])  // With Roboelectric 4.10.3 maxSdkVersion supported = 33
class GetAccountInteractorImplTest {
    private val accountManager: AccountManager = mockk()

    private lateinit var getAccountInteractor: GetAccountInteractor

    @Before
    fun setUp() {
        getAccountInteractor = GetAccountInteractorImpl(accountManager)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `GIVEN emptyArray from getAccountsByType WHEN call invoke THEN returns null`() {
        // Given
        coEvery { accountManager.getAccountsByType(any()) } returns emptyArray()

        // When
        val response = getAccountInteractor()

        // Then
        assertEquals(null, response)
    }

    @Test
    fun `GIVEN data from getAccountsByType WHEN call invoke THEN returns data`() {
        // Given
        val accountExpected = Account("name", AccountAuthenticatorConfig.ACCOUNT_TYPE)
        coEvery { accountManager.getAccountsByType(any()) } returns arrayOf(accountExpected)

        // When
        val response = getAccountInteractor()

        // Then
        assertEquals(accountExpected, response)
    }
}
