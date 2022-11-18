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

package com.leinardi.forlago.library.ui.component

import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.leinardi.forlago.library.ui.annotation.ThemePreviews
import com.leinardi.forlago.library.ui.theme.ForlagoTheme

@Suppress("UNCHECKED_CAST")
@Composable
fun DatePicker(
    show: Boolean,
    onPositiveButtonClick: (Long) -> Unit,
    selection: Long? = null,
    calendarConstraints: CalendarConstraints? = null,
    title: String? = null,
    @MaterialDatePicker.InputMode inputMode: Int = MaterialDatePicker.INPUT_MODE_CALENDAR,
    fragmentTag: String = "MaterialDatePicker",
    onNegativeButtonClick: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
) {
    // Early return if in preview
    if (LocalInspectionMode.current) {
        return
    }
    val activity = LocalContext.current as AppCompatActivity
    val fragment = remember(activity, show) { activity.supportFragmentManager.findFragmentByTag(fragmentTag) as MaterialDatePicker<Long>? }

    if (show) {
        val onPositiveButtonClickListener = MaterialPickerOnPositiveButtonClickListener<Long> { onPositiveButtonClick(it) }
        val onNegativeButtonClickListener = View.OnClickListener { onNegativeButtonClick?.invoke() }
        val onCancelListener = DialogInterface.OnCancelListener { onCancel?.invoke() }
        val onDismissListener = DialogInterface.OnDismissListener { onDismiss?.invoke() }
        if (fragment != null) {
            fragment.apply {
                clearOnPositiveButtonClickListeners()
                clearOnNegativeButtonClickListeners()
                clearOnCancelListeners()
                clearOnDismissListeners()
                addOnPositiveButtonClickListener(onPositiveButtonClickListener)
                if (onNegativeButtonClick != null) {
                    addOnNegativeButtonClickListener(onNegativeButtonClickListener)
                }
                if (onCancel != null) {
                    addOnCancelListener(onCancelListener)
                }
                if (onDismiss != null) {
                    addOnDismissListener(onDismissListener)
                }
            }
        } else {
            MaterialDatePicker.Builder.datePicker()
                .apply {
                    setSelection(selection)
                    setCalendarConstraints(calendarConstraints)
                    setTitleText(title)
                    setInputMode(inputMode)
                }
                .build()
                .run {
                    show(activity.supportFragmentManager, fragmentTag)
                    addOnPositiveButtonClickListener(onPositiveButtonClickListener)
                    if (onNegativeButtonClick != null) {
                        addOnNegativeButtonClickListener(onNegativeButtonClickListener)
                    }
                    if (onCancel != null) {
                        addOnCancelListener(onCancelListener)
                    }
                    if (onDismiss != null) {
                        addOnDismissListener(onDismissListener)
                    }
                }
        }
    } else {
        fragment?.dismiss()
    }
}

@ThemePreviews
@Composable
private fun PreviewDatePicker() {
    ForlagoTheme {
        DatePicker(show = true, onPositiveButtonClick = {})
    }
}
