import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import backend.Appwrite
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.ui.tooling.preview.Preview
import pages.AuthPage
import pages.ManagePasswordPage
import pages.HomePage
import pages.ProfilePage
import passmanager.composeapp.generated.resources.Res
import passmanager.composeapp.generated.resources.mont

@Composable
@Preview
fun App() {
	val montserrat = FontFamily(Font(Res.font.mont))
	Appwrite.initAppwrite()
	MaterialTheme(
		colors = Colors(
			background = Color.White,
			surface = Color.White,
			error = Color.Red,
			onError = Color.White,
			primary = Color.Black,
			onPrimary = Color.White,
			secondary = Color.Black,
			onSecondary = Color.White,
			onBackground = Color.Black,
			onSurface = Color.Black,
			secondaryVariant = Color.White,
			primaryVariant = Color.Black,
			isLight = true,
		),
		typography = Typography(montserrat),
	) {
		SelectionContainer {
			val navigator = rememberNavigator()
			NavHost(
				navigator = navigator,
				navTransition = NavTransition(),
				initialRoute = "/auth",
			) {
				scene(route = "/auth") {
					AuthPage(navigator)
				}
				scene(route = "/home") {
					HomePage(navigator)
				}
				scene(route = "/profile") {
					ProfilePage(navigator)
				}
				scene(route = "/manage-password/{id}") { backStackEntry ->
					ManagePasswordPage(backStackEntry.path<String>("id")!!, navigator)
				}
			}
		}
	}
}
