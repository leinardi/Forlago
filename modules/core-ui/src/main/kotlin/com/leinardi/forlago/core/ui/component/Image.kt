/*
 * Copyright 2022 Roberto Leinardi.
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

package com.leinardi.forlago.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.leinardi.forlago.core.ui.ext.default

@OptIn(ExperimentalCoilApi::class)
@Composable
fun Image(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholder: Boolean = false,
    crossfade: Boolean = true,
    @DrawableRes fallbackRes: Int? = null,
    @DrawableRes errorRes: Int? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
) {
    val painter = rememberImagePainter(
        data = url,
        builder = {
            crossfade(crossfade)
            fallbackRes?.let { fallback(it) }
            errorRes?.let { error(it) }
        },
    )
    androidx.compose.foundation.Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier  // The placeholder modifier must be added after the clip one to be rendered correctly
            .placeholder(
                visible = placeholder,
                highlight = PlaceholderHighlight.default(),
            ),
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
    )
}
