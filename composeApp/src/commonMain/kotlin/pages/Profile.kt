package pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.navigation.Navigator
import org.jetbrains.compose.resources.painterResource
import passmanager.composeapp.generated.resources.Res
import passmanager.composeapp.generated.resources.ic_back

@Composable
fun ProfilePage(navigator: Navigator) {
	var email: String? by remember { mutableStateOf(null) }
	var password: String? by remember { mutableStateOf(null) }
	
	Box(
		modifier = Modifier
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
		
	}
}