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

package com.leinardi.forlago.library.test.rule

import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.rules.ExternalResource
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

class MockWebServerRule : ExternalResource() {
    private val server = MockWebServer()
    private var started = false

    val hostName: String
        get() {
            if (!started) {
                before()
            }
            return server.hostName
        }

    val port: Int
        get() {
            if (!started) {
                before()
            }
            return server.port
        }

    val requestCount: Int
        get() = server.requestCount

    @Suppress("TooGenericExceptionThrown")
    override fun before() {
        if (started) {
            return
        }
        started = true
        try {
            server.start(PORT)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun after() {
        try {
            server.shutdown()
        } catch (e: IOException) {
            logger.log(Level.WARNING, "MockWebServer shutdown failed", e)
        }
    }

    fun enqueue(response: MockResponse) {
        server.enqueue(response)
    }

    @Throws(InterruptedException::class)
    fun takeRequest(): RecordedRequest = server.takeRequest()

    fun url(path: String): HttpUrl = server.url(path)

    /**
     * For any other functionality, use the [MockWebServer] directly.
     */
    fun get(): MockWebServer = server

    companion object {
        private const val PORT = 8000
        private val logger = Logger.getLogger(MockWebServerRule::class.java.name)
    }
}
