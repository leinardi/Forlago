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

package com.leinardi.forlago.library.ui.component

import androidx.compose.animation.core.spring
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.google.accompanist.placeholder.PlaceholderDefaults
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.material3.color
import com.google.accompanist.placeholder.material3.fadeHighlightColor
import com.google.accompanist.placeholder.placeholder

const val InitialLoadingPlaceholderCount = 6

// Switch from placeholder foundation to placeholder material once it will support Material 3
// https://github.com/google/accompanist/issues/1151
fun Modifier.placeholder(
    visible: Boolean,
): Modifier = composed {
    val containerColor = MaterialTheme.colorScheme.surface
    Modifier.placeholder(
        visible = visible,
        color = PlaceholderDefaults.color(
            backgroundColor = containerColor,
            contentColor = contentColorFor(containerColor),
        ),
        shape = MaterialTheme.shapes.small,
        highlight = PlaceholderHighlight.fade(
            highlightColor = PlaceholderDefaults.fadeHighlightColor(backgroundColor = containerColor),
            animationSpec = PlaceholderDefaults.fadeAnimationSpec,
        ),
        placeholderFadeTransitionSpec = { spring() },
        contentFadeTransitionSpec = { spring() },
    )
}
