package pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import backend.Appwrite
import backend.Password
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.Navigator
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import passmanager.composeapp.generated.resources.Res
import passmanager.composeapp.generated.resources.ic_add
import passmanager.composeapp.generated.resources.ic_person
import passmanager.composeapp.generated.resources.ic_refresh
import passmanager.composeapp.generated.resources.mont

@Composable
fun HomePage(navigator: Navigator) {
	val montserrat = FontFamily(Font(Res.font.mont))
	var passwords by remember { mutableStateOf<List<Password>>(emptyList()) }
	var refreshLoading by remember { mutableStateOf(false) }
	
	suspend fun refresh() {
		Appwrite.fetchPasswords { res ->
			res.onSuccess {
				passwords = it
			}.onFailure {
				it.printStackTrace()
			}
		}
	}
	
	CoroutineScope(Dispatchers.IO).launch { refresh() }
	
	Scaffold(
		floatingActionButton = {
			Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				if (!refreshLoading) {
					Button(
						onClick = {
							refreshLoading = true
							CoroutineScope(Dispatchers.IO).launch {
								refresh()
								refreshLoading = false
							}
						}
					) {
						Icon(
							painter = painterResource(Res.drawable.ic_refresh),
							contentDescription = null,
						)
					}
				} else {
					CircularProgressIndicator(
						strokeWidth = 2.dp,
						modifier = Modifier
							.width(32.dp)
							.height(32.dp)
					)
				}
				Box(modifier = Modifier.width((if (refreshLoading) 16 else 8).dp))
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
		}
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Top,
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp),
		) {
			Row(
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.fillMaxWidth(),
			) {
				Text(
					text = "Your passwords",
					style = TextStyle(
						fontFamily = montserrat,
						fontSize = 30.sp,
						fontWeight = FontWeight.Bold
					),
					modifier = Modifier.padding(start = 8.dp)
				)
				IconButton(
					onClick = {
						navigator.navigate("/profile")
					},
				) {
					Icon(
						painter = painterResource(Res.drawable.ic_person),
						contentDescription = null,
					)
				}
			}
			Box(modifier = Modifier.height(16.dp))
			passwords.map {
				Card(
					modifier = Modifier
						.fillMaxWidth()
						.clickable {
							navigator.navigate("/manage-password/${it.id}")
						}
						.padding(vertical = 8.dp, horizontal = 16.dp),
				) {
					Text(
						text = it.domain,
						style = TextStyle(
							fontWeight = FontWeight.Bold,
							fontSize = 30.sp,
							fontFamily = montserrat,
						),
						modifier = Modifier.padding(
							top = 16.dp,
							start = 16.dp,
							end = 16.dp,
						),
					)
					Text(
						text = "*".repeat(it.password.length),
						style = TextStyle(
							fontFamily = montserrat,
						),
						modifier = Modifier.padding(
							bottom = 16.dp,
							start = 16.dp,
							end = 16.dp,
						),
					)
				}
			}
		}
	}
}