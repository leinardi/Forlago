package com.leinardi.androidtemplateproject.home.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import com.leinardi.androidtemplateproject.ui.theme.AndroidTemplateProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidTemplateProjectTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ScreenDemo(viewModel)
                }
            }
        }
    }
}

@Composable
fun ScreenDemo(model: HomeViewModel) {
    val count by model.counterLiveData.observeAsState(0)
    Demo("This is $count") { model.increaseCounter() }
}

@Composable
fun Demo(text: String, onClick: () -> Unit = {}) {
    Column {
        BasicText(text)
        Button(
            onClick = onClick,
        ) {
            BasicText(text = "Add 1")
        }
    }
}

@Preview
@Composable
fun PreviewDemo() {
    Demo("Preview")
}
