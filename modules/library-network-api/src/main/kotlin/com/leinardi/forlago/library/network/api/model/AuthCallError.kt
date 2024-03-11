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

package com.leinardi.forlago.library.network.api.model

sealed class AuthCallError {
    data class ApiError(val errorMessages: List<String>?) : AuthCallError()
    data class NetworkError(val errorMessage: String?) : AuthCallError()
    data class UnrecoverableError(val errorMessage: String? = null) : AuthCallError()
    object ReAuthenticationRequired : AuthCallError()
}
