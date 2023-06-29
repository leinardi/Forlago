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

package com.leinardi.forlago.library.test.ext

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.ComposeTimeoutException
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso

private const val WAIT_UNTIL_TIMEOUT = 5_000L

fun ComposeContentTestRule.waitUntilNodeCount(
    matcher: SemanticsMatcher,
    count: Int,
    tapOnRetryOnFail: Boolean = false,
    timeoutMillis: Long = WAIT_UNTIL_TIMEOUT,
    useUnmergedTree: Boolean = true,
    waitUntilEqualOrBigger: Boolean = false,
) {
    var size = 0
    try {
        waitUntil(timeoutMillis) {
            size = onAllNodes(matcher, useUnmergedTree).fetchSemanticsNodes().size
            if (waitUntilEqualOrBigger) {
                size >= count
            } else {
                size == count
            }
        }
    } catch (composeTimeoutException: ComposeTimeoutException) {
        if (tapOnRetryOnFail && onAllNodes(hasText("Retry")).fetchSemanticsNodes().isNotEmpty()) {
            onNode(hasText("Retry")).performClick()
            waitUntilNodeCount(
                matcher = matcher,
                count = count,
                timeoutMillis = timeoutMillis,
                useUnmergedTree = useUnmergedTree,
                waitUntilEqualOrBigger = waitUntilEqualOrBigger,
            )
        } else {
            throw ComposeTimeoutException(
                "Condition still not satisfied after $timeoutMillis ms.\n" +
                    "Expecting to find $count nodes with matcher ${matcher.description} " +
                    "but instead found $size.",
            )
        }
    }
}

fun ComposeContentTestRule.waitUntilNodeCountEqualOrBiggerThan(
    matcher: SemanticsMatcher,
    count: Int,
    tapOnRetryOnFail: Boolean = false,
    timeoutMillis: Long = WAIT_UNTIL_TIMEOUT,
    useUnmergedTree: Boolean = true,
) {
    waitUntilNodeCount(
        matcher = matcher,
        count = count,
        tapOnRetryOnFail = tapOnRetryOnFail,
        timeoutMillis = timeoutMillis,
        useUnmergedTree = useUnmergedTree,
        waitUntilEqualOrBigger = true,
    )
}

fun ComposeContentTestRule.waitUntilExists(
    matcher: SemanticsMatcher,
    tapOnRetryOnFail: Boolean = false,
    timeoutMillis: Long = WAIT_UNTIL_TIMEOUT,
    useUnmergedTree: Boolean = true,
) {
    waitUntilNodeCount(
        matcher = matcher,
        count = 1,
        tapOnRetryOnFail = tapOnRetryOnFail,
        timeoutMillis = timeoutMillis,
        useUnmergedTree = useUnmergedTree,
    )
}

fun ComposeContentTestRule.waitUntilExists(
    testTag: String,
    tapOnRetryOnFail: Boolean = false,
    timeoutMillis: Long = WAIT_UNTIL_TIMEOUT,
    useUnmergedTree: Boolean = true,
) {
    waitUntilExists(
        matcher = hasTestTag(testTag),
        tapOnRetryOnFail = tapOnRetryOnFail,
        timeoutMillis = timeoutMillis,
        useUnmergedTree = useUnmergedTree,
    )
}

fun ComposeContentTestRule.waitUntilAtLeastOneExistsInUnmergedTree(
    matcher: SemanticsMatcher,
    tapOnRetryOnFail: Boolean = false,
    timeoutMillis: Long = WAIT_UNTIL_TIMEOUT,
    useUnmergedTree: Boolean = true,
) {
    waitUntilNodeCountEqualOrBiggerThan(
        matcher = matcher,
        count = 1,
        tapOnRetryOnFail = tapOnRetryOnFail,
        timeoutMillis = timeoutMillis,
        useUnmergedTree = useUnmergedTree,
    )
}

fun ComposeContentTestRule.waitUntilAtLeastOneExistsInUnmergedTree(
    testTag: String,
    tapOnRetryOnFail: Boolean = false,
    timeoutMillis: Long = WAIT_UNTIL_TIMEOUT,
    useUnmergedTree: Boolean = true,
) {
    waitUntilAtLeastOneExistsInUnmergedTree(
        matcher = hasTestTag(testTag),
        tapOnRetryOnFail = tapOnRetryOnFail,
        timeoutMillis = timeoutMillis,
        useUnmergedTree = useUnmergedTree,
    )
}

fun ComposeContentTestRule.waitUntilDoesNotExist(
    testTag: String,
    ignoreException: Boolean = false,
    tapOnRetryOnFail: Boolean = false,
    timeoutMillis: Long = WAIT_UNTIL_TIMEOUT,
    useUnmergedTree: Boolean = true,
): Boolean {
    try {
        waitUntilNodeCount(
            matcher = hasTestTag(testTag),
            count = 0,
            tapOnRetryOnFail = tapOnRetryOnFail,
            timeoutMillis = timeoutMillis,
            useUnmergedTree = useUnmergedTree,
        )
        return true
    } catch (composeTimeoutException: ComposeTimeoutException) {
        if (ignoreException) {
            return false
        }
        throw composeTimeoutException
    }
}

fun ComposeContentTestRule.waitUntilDoesNotExist(
    matcher: SemanticsMatcher,
    ignoreException: Boolean = false,
    tapOnRetryOnFail: Boolean = false,
    timeoutMillis: Long = WAIT_UNTIL_TIMEOUT,
    useUnmergedTree: Boolean = true,
): Boolean {
    try {
        waitUntilNodeCount(
            matcher = matcher,
            count = 0,
            tapOnRetryOnFail = tapOnRetryOnFail,
            timeoutMillis = timeoutMillis,
            useUnmergedTree = useUnmergedTree,
        )
        return true
    } catch (composeTimeoutException: ComposeTimeoutException) {
        if (ignoreException) {
            return false
        }
        throw composeTimeoutException
    }
}

fun ComposeContentTestRule.waitUntilExistsAndTap(
    testTag: String,
    tapOnRetryOnFail: Boolean = false,
    isScrollToElement: Boolean = false,
) {
    waitUntilExistsAndTap(
        matcher = hasTestTag(testTag),
        tapOnRetryOnFail = tapOnRetryOnFail,
        isScrollToElement = isScrollToElement,
    )
}

fun ComposeContentTestRule.waitUntilExistsAndTap(
    matcher: SemanticsMatcher,
    tapOnRetryOnFail: Boolean = false,
    isScrollToElement: Boolean = false,
) {
    if (isScrollToElement) {
        onNode(matcher, true).performScrollTo()
    }
    waitUntilExists(matcher = matcher, tapOnRetryOnFail = tapOnRetryOnFail)
    onNode(matcher, true).performClick()
}

fun ComposeContentTestRule.waitUntilExistsAndTapOnFirst(
    testTag: String,
    tapOnRetryOnFail: Boolean = false,
) {
    waitUntilExistsAndTapOnFirst(
        matcher = hasTestTag(testTag),
        tapOnRetryOnFail = tapOnRetryOnFail,
    )
}

fun ComposeContentTestRule.waitUntilExistsAndTapOnFirst(
    matcher: SemanticsMatcher,
    tapOnRetryOnFail: Boolean = false,
) {
    waitUntilExistsAndTapOnIndex(
        matcher = matcher,
        index = 0,
        tapOnRetryOnFail = tapOnRetryOnFail,
    )
}

fun ComposeContentTestRule.waitUntilExistsAndTapOnIndex(
    testTag: String,
    index: Int,
    tapOnRetryOnFail: Boolean = false,
) {
    waitUntilNodeCountEqualOrBiggerThan(
        matcher = hasTestTag(testTag),
        count = index + 1,
        tapOnRetryOnFail = tapOnRetryOnFail,
    )
    onAllNodes(matcher = hasTestTag(testTag), useUnmergedTree = true)[index].performClick()
}

fun ComposeContentTestRule.waitUntilExistsAndTapOnIndex(
    matcher: SemanticsMatcher,
    index: Int,
    tapOnRetryOnFail: Boolean = false,
) {
    waitUntilNodeCountEqualOrBiggerThan(matcher = matcher, count = index + 1, tapOnRetryOnFail = tapOnRetryOnFail)
    onAllNodes(matcher = matcher, useUnmergedTree = true)[index].performClick()
}

fun ComposeContentTestRule.waitUntilExistsTapAndSendText(
    testTag: String,
    inputText: String,
    tapOnRetryOnFail: Boolean = false,
) {
    waitUntilExistsAndTap(testTag = testTag, tapOnRetryOnFail = tapOnRetryOnFail)
    onNodeWithTag(
        testTag = testTag,
        useUnmergedTree = true,
    ).performTextInput(inputText)
    Espresso.closeSoftKeyboard()
}

fun ComposeContentTestRule.waitUntilExistsTapAndSendText(
    matcher: SemanticsMatcher,
    inputText: String,
    tapOnRetryOnFail: Boolean = false,
) {
    waitUntilExistsAndTap(matcher = matcher, tapOnRetryOnFail = tapOnRetryOnFail)
    onNode(
        matcher = matcher,
        useUnmergedTree = true,
    ).performTextInput(inputText)
    Espresso.closeSoftKeyboard()
}

fun ComposeContentTestRule.waitUntilExistsAndClear(
    matcher: SemanticsMatcher,
    tapOnRetryOnFail: Boolean = false,
) {
    waitUntilExistsAndTap(matcher = matcher, tapOnRetryOnFail = tapOnRetryOnFail)
    onNode(
        matcher = matcher,
        useUnmergedTree = true,
    ).performTextClearance()
    Espresso.closeSoftKeyboard()
}

fun ComposeContentTestRule.waitForConditionWhilePerformAction(
    condition: () -> Boolean,
    action: () -> Unit,
    timeoutMillis: Long = WAIT_UNTIL_TIMEOUT,
) {
    waitUntil(timeoutMillis = timeoutMillis) {
        action()
        condition()
    }
}

fun ComposeContentTestRule.isElementPresent(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = WAIT_UNTIL_TIMEOUT,
): Boolean = try {
    waitUntil(timeoutMillis = timeoutMillis) {
        onAllNodes(matcher, true).fetchSemanticsNodes().isNotEmpty()
    }
    true
} catch (composeTimeoutException: ComposeTimeoutException) {
    false
}

fun ComposeContentTestRule.getText(
    testTag: String,
    includeEditableText: Boolean = true,
    index: Int = 0,
): String = getText(hasTestTag(testTag), includeEditableText, index)

fun ComposeContentTestRule.getText(
    matcher: SemanticsMatcher,
    includeEditableText: Boolean = true,
    index: Int = 0,
): String {
    require(onAllNodes(matcher, true).fetchSemanticsNodes().isNotEmpty())
    val actual = mutableListOf<String>()
    onAllNodes(matcher = matcher, useUnmergedTree = true).fetchSemanticsNodes()[index].let { node ->
        if (includeEditableText) {
            node.config.getOrNull(SemanticsProperties.EditableText)?.let { actual.add(it.text) }
        }
        node.config.getOrNull(SemanticsProperties.Text)?.let { actual.addAll(it.map { anStr -> anStr.text }) }
    }
    return actual.first()
}

fun ComposeContentTestRule.wait(timeoutMillis: Long = 10_000L) {
    try {
        waitUntil(timeoutMillis) { false }
    } catch (composeTimeoutException: ComposeTimeoutException) {
        composeTimeoutException.stackTrace
    }
}
