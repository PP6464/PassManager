package pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import backend.Appwrite
import backend.Validator
import io.appwrite.exceptions.AppwriteException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.Navigator
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import passmanager.composeapp.generated.resources.Res
import passmanager.composeapp.generated.resources.ic_back
import passmanager.composeapp.generated.resources.ic_email
import passmanager.composeapp.generated.resources.ic_lock
import passmanager.composeapp.generated.resources.ic_visibility
import passmanager.composeapp.generated.resources.ic_visibility_off
import passmanager.composeapp.generated.resources.mont

@Composable
fun ProfilePage(navigator: Navigator) {
	val montserrat = FontFamily(Font(Res.font.mont))
	var email by remember { mutableStateOf(Appwrite.currentUser!!.email) }
	var password by remember { mutableStateOf("") }
	var emailError: String? by remember { mutableStateOf(null) }
	var passwordError: String? by remember { mutableStateOf(null) }
	var obscurePassword by remember { mutableStateOf(true) }
	
	Column(
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
			style = TextStyle(
				fontFamily = montserrat,
				fontSize = 20.sp,
				fontWeight = FontWeight.Bold,
			),
		)
		Box(modifier = Modifier.height(8.dp))
		OutlinedTextField(
			value = email,
			onValueChange = {
				email = it
				emailError = null
			},
			supportingText = {
				if (emailError == null) {
					Box(
						modifier = Modifier
							.width(0.dp)
							.height(0.dp)
					)
				} else {
					Text(text = emailError!!, color = Color.Red)
				}
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
		Box(modifier = Modifier.height(8.dp))
		OutlinedTextField(
			value = password,
			visualTransformation = if (obscurePassword) PasswordVisualTransformation() else VisualTransformation.None,
			onValueChange = {
				password = it
				passwordError = null
			},
			textStyle = TextStyle(
				fontFamily = montserrat,
			),
			placeholder = {
				Text(
					text = "Password",
					style = TextStyle(
						fontFamily = montserrat,
						color = Color.Gray,
					)
				)
			},
			leadingIcon = {
				Icon(
					painter = painterResource(Res.drawable.ic_lock),
					contentDescription = null,
				)
			},
			trailingIcon = {
				IconButton(
					onClick = {
						obscurePassword = !obscurePassword
					},
				) {
					Icon(
						painter = painterResource(if (obscurePassword) Res.drawable.ic_visibility else Res.drawable.ic_visibility_off),
						contentDescription = null,
					)
				}
			},
			supportingText = {
				if (passwordError == null) {
					Box(
						modifier = Modifier
							.width(0.dp)
							.height(0.dp)
					)
				} else {
					androidx.compose.material.Text(text = passwordError!!, color = Color.Red)
				}
			},
		)
		Box(modifier = Modifier.height(8.dp))
		Button(
			onClick = {
				if (!Validator.isValidEmail(email)) {
					emailError = "Invalid email"
				}
				if (!Validator.isValidPassword(password)) {
					passwordError = "Password must have:\n - 1 special character\n - 1 uppercase letter\n - 1 number\nand be at least 10 characters long"
				}
				if (emailError != null || passwordError != null) {
					return@Button
				}
				
				CoroutineScope(Dispatchers.IO).launch {
					Appwrite.changeProfile(email = email, password = password) { res ->
						res.onSuccess {
						
						}.onFailure { e ->
							if ((e as AppwriteException).type == "password_personal_data") {
								passwordError = "Password shouldn't have name or email in it"
								return@onFailure
							}
							
							if ((e.type?.contains("user")) == true || (e.type?.contains("password")) == true) {
								passwordError = e.message ?: "Invalid password"
								return@onFailure
							}
							
							e.printStackTrace()
						}
						
					}
				}
			},
		) {
			Text(
				text = "Save changes",
				style = TextStyle(
					fontFamily = montserrat,
				)
			)
		}
	}
}