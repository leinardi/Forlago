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

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leinardi.forlago.core.ui.theme.ForlagoTheme

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
    errorMessage: String? = null,
    error: Boolean = errorMessage != null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    passwordToggleEnabled: Boolean = false,
    keyboardOptions: KeyboardOptions = if (passwordToggleEnabled) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
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
            error -> Icon(Icons.Filled.Error, null)
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
    Column {
        androidx.compose.material.OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            label = { label?.let { Text(text = it) } },
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = textFieldTrailingIcon,
            isError = error,
            visualTransformation = passwordToggleVisualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors,
        )
        Box(
            modifier = Modifier
                .defaultMinSize(minHeight = 16.dp)
                .padding(start = 16.dp, end = 12.dp),
        ) {
            if (error) {
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                    )
                }
            } else {
                if (helperMessage != null) {
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.medium,
                    ) {
                        Text(
                            text = helperMessage,
                            style = MaterialTheme.typography.caption,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOutlinedTextField() {
    ForlagoTheme {
        OutlinedTextField(value = "input", onValueChange = {}, label = "Label")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOutlinedTextFieldEmpty() {
    ForlagoTheme {
        OutlinedTextField(value = "", onValueChange = {}, label = "Label")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOutlinedTextFieldErrorMessage() {
    ForlagoTheme {
        OutlinedTextField(value = "input", onValueChange = {}, label = "Label", errorMessage = "Error message")
    }
}
