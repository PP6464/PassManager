package pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import backend.Appwrite
import moe.tlaster.precompose.navigation.Navigator
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import passmanager.composeapp.generated.resources.Res
import passmanager.composeapp.generated.resources.ic_back
import passmanager.composeapp.generated.resources.ic_email
import passmanager.composeapp.generated.resources.mont

@Composable
fun ProfilePage(navigator: Navigator) {
	val montserrat = FontFamily(Font(Res.font.mont))
	var email by remember { mutableStateOf(Appwrite.currentUser!!.email) }
	var password by remember { mutableStateOf(Appwrite.currentUser!!.password!!) }
	
	Box(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
	) {
		IconButton(
			onClick = {
				navigator.goBack()
			},
		) {
			Icon(
				painter = painterResource(Res.drawable.ic_back),
				contentDescription = null,
			)
		}
		Text(
			text = "Your profile",
		)
		OutlinedTextField(
			value = email,
			onValueChange = {
				email = it
			},
			leadingIcon = {
				Icon(
					painter = painterResource(Res.drawable.ic_email),
					contentDescription = null,
				)
			},
			placeholder = {
				Text(
					text = "Email",
					style = TextStyle(
						color = Color.Gray,
					),
				)
			},
			textStyle = TextStyle(
				fontFamily = montserrat,
			),
		)
	}
}