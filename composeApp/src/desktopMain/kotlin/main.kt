import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import moe.tlaster.precompose.PreComposeApp
import org.jetbrains.compose.resources.painterResource
import passmanager.composeapp.generated.resources.Res
import passmanager.composeapp.generated.resources.logo

fun main() = application {
	Window(
		onCloseRequest = ::exitApplication,
		icon = painterResource(Res.drawable.logo),
		title = "PassManager",
	) {
		PreComposeApp {
			App()
		}
	}
}