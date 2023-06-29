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

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.leinardi.forlago.library.ui.annotation.ThemePreviews
import com.leinardi.forlago.library.ui.constraintlayout.goneIf
import com.leinardi.forlago.library.ui.theme.ForlagoTheme
import com.leinardi.forlago.library.ui.theme.Spacing

@Suppress("ReusedModifierInstance")
@Composable
fun OutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: String? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    helperMessage: String? = null,
    counterMessage: String? = null,
    errorMessage: String? = null,
    isError: Boolean = errorMessage != null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    passwordToggleEnabled: Boolean = false,
    keyboardOptions: KeyboardOptions = if (passwordToggleEnabled) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    var passwordVisibility: Boolean by remember { mutableStateOf(!passwordToggleEnabled) }
    val passwordToggleVisualTransformation = if (passwordToggleEnabled) {
        if (passwordVisibility) visualTransformation else PasswordVisualTransformation()
    } else {
        visualTransformation
    }

    val passwordToggleIcon = if (passwordVisibility) {
        Icons.Filled.Visibility
    } else {
        Icons.Filled.VisibilityOff
    }

    val textFieldTrailingIcon: @Composable (() -> Unit) = {
        when {
            isError -> Icon(Icons.Filled.Error, null)
            passwordToggleEnabled -> IconButton(
                onClick = {
                    passwordVisibility = !passwordVisibility
                },
            ) {
                Icon(passwordToggleIcon, null)
            }
            else -> trailingIcon?.invoke()
        }
    }
    ConstraintLayout {
        val (textFieldRef, helperMessageRef, counterMessageRef) = createRefs()
        androidx.compose.material3.OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.constrainAs(textFieldRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            label = { label?.let { Text(text = it) } },
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = textFieldTrailingIcon,
            isError = isError,
            visualTransformation = passwordToggleVisualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors,
        )
        Text(
            text = if (isError && errorMessage != null) errorMessage else helperMessage.orEmpty(),
            color = if (isError && errorMessage != null) MaterialTheme.colorScheme.error else Color.Unspecified,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .constrainAs(helperMessageRef) {
                    top.linkTo(textFieldRef.bottom)
                    start.linkTo(textFieldRef.start, Spacing.x02)
                    end.linkTo(counterMessageRef.start, Spacing.x01, goneMargin = 12.dp)
                    width = Dimension.fillToConstraints
                    visibility = goneIf { if (isError) errorMessage == null else helperMessage == null }
                }
                .defaultMinSize(minHeight = 16.dp),
        )
        Text(
            text = counterMessage.orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .constrainAs(counterMessageRef) {
                    top.linkTo(textFieldRef.bottom)
                    end.linkTo(textFieldRef.end, 12.dp)
                    width = Dimension.fillToConstraints
                    visibility = goneIf { counterMessage == null }
                }
                .defaultMinSize(minHeight = 16.dp),
        )
    }
}

@ThemePreviews
@Composable
private fun PreviewOutlinedTextField() {
    ForlagoTheme {
        OutlinedTextField(value = "input", onValueChange = {}, label = "Label")
    }
}

@ThemePreviews
@Composable
private fun PreviewOutlinedTextFieldEmpty() {
    ForlagoTheme {
        OutlinedTextField(value = "", onValueChange = {}, label = "Label")
    }
}

@ThemePreviews
@Composable
private fun PreviewOutlinedTextFieldEmptyHelperMessage() {
    ForlagoTheme {
        OutlinedTextField(value = "", onValueChange = {}, label = "Label", helperMessage = "Helper message")
    }
}

@ThemePreviews
@Composable
private fun PreviewOutlinedTextFieldEmptyHelperMessageCounterMessage() {
    ForlagoTheme {
        OutlinedTextField(value = "input", onValueChange = {}, label = "Label", helperMessage = "Helper text", counterMessage = "5/160")
    }
}

@ThemePreviews
@Composable
private fun PreviewOutlinedTextFieldErrorMessage() {
    ForlagoTheme {
        OutlinedTextField(value = "input", onValueChange = {}, label = "Label", errorMessage = "Error message")
    }
}

@ThemePreviews
@Composable
private fun PreviewOutlinedTextFieldErrorMessageCounterMessage() {
    ForlagoTheme {
        OutlinedTextField(value = "input", onValueChange = {}, label = "Label", errorMessage = "Error message", counterMessage = "5/160")
    }
}
