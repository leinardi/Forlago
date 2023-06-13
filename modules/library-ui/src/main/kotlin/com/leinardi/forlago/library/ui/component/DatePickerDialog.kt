package com.leinardi.forlago.library.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.leinardi.forlago.library.ui.annotation.ThemePreviews
import com.leinardi.forlago.library.ui.theme.ForlagoTheme
import com.leinardi.forlago.library.ui.theme.Spacing

@Composable
fun DatePickerDialog(
    show: Boolean,
    onPositiveButtonClick: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    selection: Long? = null,
    dateValidator: (Long) -> Boolean = { true },
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
            androidx.compose.material3.DatePicker(
                state = datePickerState,
                dateValidator = dateValidator,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun PreviewDatePickerDialog() {
    ForlagoTheme {
        DatePickerDialog(show = true, onPositiveButtonClick = {})
    }
}
