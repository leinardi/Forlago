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

import android.accounts.AccountManager
import com.github.michaelbull.result.Ok
import com.leinardi.forlago.feature.account.api.interactor.account.AddAccountInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.GetAccessTokenInteractor
import com.leinardi.forlago.library.android.api.coroutine.CoroutineDispatchers
import com.leinardi.forlago.library.android.api.interactor.encryption.EncryptInteractor
import com.leinardi.forlago.library.test.coroutine.MainDispatcherRule
import com.leinardi.forlago.library.test.coroutine.coroutineTestDispatchers
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class AddAccountInteractorImplTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()
    private lateinit var addAccountInteractor: AddAccountInteractor
    private val accountManager: AccountManager = mockk()
    private val dispatchers: CoroutineDispatchers = coroutineTestDispatchers
    private val encryptInteractor: EncryptInteractor = mockk()
    private val getAccessTokenInteractor: GetAccessTokenInteractor = mockk()

    @Before
    fun setUp() {
        addAccountInteractor = AddAccountInteractorImpl(accountManager, dispatchers, encryptInteractor, getAccessTokenInteractor)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `GIVEN data WHEN call invoke THEN return result`(): TestResult = runTest {
        // Given
        coEvery { encryptInteractor(any<String>()) } returns "encrypted"
        coEvery { getAccessTokenInteractor() } returns Ok("accessToken")
        val responseExpected = false
        every { accountManager.addAccountExplicitly(any(), any(), any()) } returns responseExpected

        // When
        val response = addAccountInteractor("name", "refreshToken")

        // Then
        coVerify { encryptInteractor(any<String>()) }
        coVerify { getAccessTokenInteractor() }
        assertEquals(response, responseExpected)
    }
}
