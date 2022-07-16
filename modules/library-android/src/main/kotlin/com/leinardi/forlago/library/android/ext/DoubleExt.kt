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

package com.leinardi.forlago.library.android.ext

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

fun Double.formatCurrency(locale: Locale): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance(locale)
    if (format is DecimalFormat) {
        // use local/default decimal symbols with original currency symbol
        val decimalFormatSymbols: DecimalFormatSymbols = DecimalFormat().decimalFormatSymbols.apply {
            currency = format.currency
        }
        format.decimalFormatSymbols = decimalFormatSymbols
    }
    return format.format(this)
}

fun Double.formatUsCurrency() = formatCurrency(Locale.US)
