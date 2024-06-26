package io.github.pp6464.passmanager

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import moe.tlaster.precompose.PreComposeApp

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		setContent {
			PreComposeApp {
				App()
			}
		}
	}
}

@Preview
@Composable
fun AppAndroidPreview() {
	App()
}