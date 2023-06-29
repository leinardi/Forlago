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

package com.leinardi.forlago.feature.account

object AccountAuthenticatorConfig {
    const val ACCOUNT_TYPE: String = BuildConfig.ACCOUNT_TYPE
    const val AUTH_TOKEN_TYPE = "defaultAuthToken"  // https://stackoverflow.com/q/25056112/293878
    const val KEY_IS_NEW_ACCOUNT = "isNewAccount"
}
