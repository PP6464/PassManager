package backend

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalComposeUiApi::class)
actual fun getScreenHeight() : Dp {
	return LocalWindowInfo.current.containerSize.height.dp
}