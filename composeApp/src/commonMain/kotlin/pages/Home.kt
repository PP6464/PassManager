package pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import backend.Appwrite
import moe.tlaster.precompose.navigation.Navigator
import org.jetbrains.compose.resources.Font
import passmanager.composeapp.generated.resources.Res
import passmanager.composeapp.generated.resources.mont

@Composable
fun HomePage(navigator: Navigator) {
	val montserrat = FontFamily(Font(Res.font.mont))
	
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
		modifier = Modifier
			.fillMaxSize(),
	) {
		Text(
			text = Appwrite.currentUser!!.email,
			style = TextStyle(
				fontFamily = montserrat,
			),
		)
	}
}