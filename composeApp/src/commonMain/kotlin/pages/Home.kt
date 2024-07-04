package pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import backend.Appwrite
import moe.tlaster.precompose.navigation.Navigator
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import passmanager.composeapp.generated.resources.Res
import passmanager.composeapp.generated.resources.ic_add
import passmanager.composeapp.generated.resources.mont

@Composable
fun HomePage(navigator: Navigator) {
	val montserrat = FontFamily(Font(Res.font.mont))
	
	Scaffold(
		floatingActionButton = {
			Button(
				onClick = {
					println("HI")
				}
			) {
				Icon(
					painter = painterResource(Res.drawable.ic_add),
					contentDescription = null
				)
			}
		}
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Top,
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp),
		) {
			Text(
				text = "Your passwords",
				style = TextStyle(
					fontFamily = montserrat,
					fontSize = 30.sp,
					fontWeight = FontWeight.Bold
				),
			)
			
		}
	}
}