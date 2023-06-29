/*
 * Copyright 2023 Roberto Leinardi.
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

package com.leinardi.forlago.library.android.api.ext

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CharSequenceExtKtTest {
    @Test
    fun `GIVEN valid email WHEN isValidEmail THEN returns true`() {
        // Given
        var email = "firstname.lastname@domain.com"

        // When
        var result = email.isValidEmail()

        // Then
        assertTrue(result)

        // Given
        email = "firstname@domain.com"

        // When
        result = email.isValidEmail()

        // Then
        assertTrue(result)

        // Given
        email = "f@d.co"

        // When
        result = email.isValidEmail()

        // Then
        assertTrue(result)
    }

    @Test
    fun `GIVEN invalid email WHEN isValidEmail THEN returns false`() {
        // Given
        var email = "firstname.lastname@domain"

        // When
        var result = email.isValidEmail()

        // Then
        assertFalse(result)

        // Given
        email = "firstname.lastname"

        // When
        result = email.isValidEmail()

        // Then
        assertFalse(result)

        // Given
        email = ""

        // When
        result = email.isValidEmail()

        // Then
        assertFalse(result)
    }
}
