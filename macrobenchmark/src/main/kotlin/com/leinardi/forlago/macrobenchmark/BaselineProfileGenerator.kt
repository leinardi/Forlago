package com.leinardi.forlago.macrobenchmark

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class BaselineProfileGenerator {
    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() {
        rule.collectBaselineProfile(
            packageName = BuildConfig.APPLICATION_ID.removeSuffix(".macrobenchmark"),
            iterations = 1,
        ) {
            startApplicationJourney()
        }
    }
}

private fun MacrobenchmarkScope.startApplicationJourney() {
    pressHome()
    startActivityAndWait()
}
