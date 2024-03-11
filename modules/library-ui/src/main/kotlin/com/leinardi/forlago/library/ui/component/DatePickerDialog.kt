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

package com.leinardi.forlago.library.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.leinardi.forlago.library.ui.preview.PreviewThemes
import com.leinardi.forlago.library.ui.theme.ForlagoTheme
import com.leinardi.forlago.library.ui.theme.Spacing

@Composable
fun DatePickerDialog(
    show: Boolean,
    onPositiveButtonClick: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    selection: Long? = null,
    selectableDates: SelectableDates = object : SelectableDates {},
    yearRange: IntRange = DatePickerDefaults.YearRange,
    initialDisplayMode: DisplayMode = DisplayMode.Picker,
    onNegativeButtonClick: (() -> Unit)? = null,
    onDismissRequest: (() -> Unit)? = null,
) {
    if (show) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selection,
            yearRange = yearRange,
            initialDisplayMode = initialDisplayMode,
            selectableDates = selectableDates,
        )
        val confirmEnabled = remember { derivedStateOf { datePickerState.selectedDateMillis != null } }
        DatePickerDialog(
            onDismissRequest = { onDismissRequest?.invoke() },
            confirmButton = {
                TextButton(
                    onClick = { onPositiveButtonClick(datePickerState.selectedDateMillis) },
                    enabled = confirmEnabled.value,
                ) {
                    Text(stringResource(android.R.string.ok))
                }
            },
            modifier = modifier.padding(horizontal = Spacing.x08),
            dismissButton = {
                TextButton(onClick = { onNegativeButtonClick?.invoke() }) {
                    Text(stringResource(android.R.string.cancel))
                }
            },
        ) {
            androidx.compose.material3.DatePicker(state = datePickerState)
        }
    }
}

@PreviewThemes
@Composable
private fun PreviewDatePickerDialog() {
    ForlagoTheme {
        DatePickerDialog(show = true, onPositiveButtonClick = {})
    }
}
