/*
 * Copyright 2021 Roberto Leinardi.
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

package com.leinardi.forlago.feature.foo.ui.foodialog

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.leinardi.forlago.feature.foo.ui.foodialog.FooDialogContract.Event

@Composable
fun FooDialogScreen(viewModel: FooDialogViewModel = hiltViewModel()) {
    FooDialogScreen(
        sendEvent = { viewModel.onUiEvent(it) },
    )
}

@Composable
fun FooDialogScreen(
    sendEvent: (event: Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
            // sendEvent(Event.OnDismissButtonClicked)
        },
        title = {
            Text(text = "title")
        },
        text = {
            Text(text = "message")
        },
        confirmButton = {
            TextButton(
                onClick = { sendEvent(Event.OnConfirmButtonClicked) }) {
                Text("Confirm Button")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { sendEvent(Event.OnDismissButtonClicked) }) {
                Text("Dismiss Button")
            }
        },
        modifier = modifier,
    )
}

@Preview
@Composable
fun FooScreenPreview() {
    FooDialogScreen({})
}
