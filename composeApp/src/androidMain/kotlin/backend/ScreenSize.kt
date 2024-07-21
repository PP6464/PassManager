package backend

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
actual fun getScreenHeight() : Dp {
	return LocalConfiguration.current.screenHeightDp.dp
}