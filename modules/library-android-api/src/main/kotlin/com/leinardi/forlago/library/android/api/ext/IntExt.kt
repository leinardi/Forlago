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

package com.leinardi.forlago.library.android.api.ext

val Int.toDp: Float
    get() = toFloat().toDp

val Int.toPx: Float
    get() = toFloat().toPx

val Int.toPxSize: Int
    get() = toFloat().toPxSize

val Int.toCoercedPercentageFloat: Float
    get() = (toFloat() / 100).coerceIn(0f, 1f)

val Int.getPercentageString: String
    get() = "$this%"
