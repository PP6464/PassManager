package pages

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import backend.Appwrite
import moe.tlaster.precompose.navigation.Navigator
import org.jetbrains.compose.resources.Font
import passmanager.composeapp.generated.resources.Res
import passmanager.composeapp.generated.resources.mont

@Composable
fun HomePage(navigator: Navigator) {
	val montserrat = FontFamily(Font(Res.font.mont))
	
	Text(Appwrite.currentUser!!.email)
}