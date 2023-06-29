package com.leinardi.forlago.library.test.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Reusable JUnit4 TestRule to override the Main dispatcher
 */
class MainDispatcherRule : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(coroutineTestDispatchers.main)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
