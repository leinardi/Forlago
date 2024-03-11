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

package com.leinardi.forlago.library.network.api.interactor

import java.time.Instant
import java.time.temporal.ChronoUnit

interface ReadEnvironmentInteractor {
    suspend operator fun invoke(): Environment

    enum class Environment(
        val restUrl: String,
        val graphQlUrl: String,
        val certificatePinningConfigs: List<CertificatePinningConfig> = emptyList(),
    ) {
        DEV("https://api.dev.example.com", "https://apollo-fullstack-tutorial.herokuapp.com/graphql"),
        MOCK("https://localhost:8000", "https://localhost:8000"),
        PROD("https://api.example.com", "https://apollo-fullstack-tutorial.herokuapp.com/graphql"),
        STAGE("https://api.stage.example.com", "https://apollo-fullstack-tutorial.herokuapp.com/graphql"),
    }
}

data class CertificatePinningConfig(
    val domain: String,
    val hashes: List<Hash>,
) {
    data class Hash(
        val hash: String,
        val expirationDate: Instant,
    )

    fun isExpiring() = hashes.none {
        Instant.now().isBefore(it.expirationDate.minus(DAYS_BEFORE_CERTIFICATE_EXPIRES, ChronoUnit.DAYS))
    }

    companion object {
        private const val DAYS_BEFORE_CERTIFICATE_EXPIRES = 30L
    }
}
