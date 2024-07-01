package pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAny
import backend.Appwrite
import backend.Validator
import io.appwrite.exceptions.AppwriteException
import moe.tlaster.precompose.navigation.Navigator
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import passmanager.composeapp.generated.resources.Res
import passmanager.composeapp.generated.resources.ic_email
import passmanager.composeapp.generated.resources.ic_lock
import passmanager.composeapp.generated.resources.ic_visibility
import passmanager.composeapp.generated.resources.ic_visibility_off
import passmanager.composeapp.generated.resources.logo
import passmanager.composeapp.generated.resources.mont

@Composable
fun AuthPage(navigator: Navigator) {
	val montserrat = FontFamily(Font(Res.font.mont))
	var progress by remember { mutableStateOf("email") }
	var email by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }
	var obscurePassword by remember { mutableStateOf(true) }
	var emailExists by remember { mutableStateOf(false) }
	var emailError by remember { mutableStateOf<String?>(null) }
	var passwordError by remember { mutableStateOf<String?>(null) }
	var loading by remember { mutableStateOf(false) }
	val focusManager = LocalFocusManager.current
	
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
	) {
		Image(
			painter = painterResource(Res.drawable.logo),
			contentDescription = "logo"
		)
		Box(
			modifier = Modifier
				.height(16.dp)
		)
		OutlinedTextField(
			value = email,
			textStyle = TextStyle(
				fontFamily = montserrat,
			),
			colors = OutlinedTextFieldDefaults.colors(
				cursorColor = Color.Black,
				errorBorderColor = Color.Red,
				focusedBorderColor = Color.Black,
			),
			isError = emailError != null,
			placeholder = { Text(text = "Email", color = Color.Gray) },
			onValueChange = {
				email = it
				emailError = null
			},
			keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
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
			maxLines = 1,
			modifier = Modifier
				.width(400.dp)
				.onKeyEvent { key ->
					if (key.key == Key.Enter) {
						if (!Validator.isValidEmail(email)) {
							emailError = "Invalid email"
							return@onKeyEvent false
						}
						progress = "password"
						Appwrite.userExists(email) { res ->
							res.onSuccess {
								emailExists = true
								focusManager.moveFocus(FocusDirection.Down)
							}.onFailure {
								if (it.message == "User not found") {
									emailExists = false
								} else {
									it.printStackTrace()
								}
							}
						}
						return@onKeyEvent true
					}
					false
				}
		)
		Box(
			modifier = Modifier
				.height(16.dp)
		)
		AnimatedVisibility(visible = progress == "password") {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center,
			) {
				OutlinedTextField(
					value = password,
					onValueChange = {
						password = it
						passwordError = null
					},
					colors = OutlinedTextFieldDefaults.colors(
						errorBorderColor = Color.Red,
						focusedBorderColor = Color.Black,
						cursorColor = Color.Black,
					),
					textStyle = TextStyle(
						fontFamily = montserrat,
					),
					keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
					isError = passwordError != null,
					supportingText = {
						if (passwordError == null) {
							Box(
								modifier = Modifier
									.width(0.dp)
									.height(0.dp)
							)
						} else {
							Text(text = passwordError!!, color = Color.Red)
						}
					},
					placeholder = { Text(text = "Password", color = Color.Gray) },
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
								painter = if (obscurePassword) {
									painterResource(Res.drawable.ic_visibility)
								} else {
									painterResource(Res.drawable.ic_visibility_off)
								},
								contentDescription = null,
							)
						}
					},
					maxLines = 1,
					visualTransformation = if (obscurePassword) {
						PasswordVisualTransformation()
					} else {
						VisualTransformation.None
					},
					modifier = Modifier
						.width(400.dp)
						.onKeyEvent { key ->
							if (key.key == Key.Enter) {
								// Check if password is valid for someone signing up
								if (!emailExists && !Validator.isValidPassword(password)) {
									passwordError =
										"Password must have:\n - 1 special character\n - 1 uppercase letter\n - 1 number\nand be at least 10 characters long"
									return@onKeyEvent false
								}
								focusManager.moveFocus(FocusDirection.Down)
							}
							false
						},
				)
				Box(
					modifier = Modifier
						.height(16.dp)
				)
				if (emailExists) {
					Button(
						onClick = {
							loading = true
							Appwrite.login(email, password) { res ->
								res.onSuccess {
									loading = false
									navigator.navigate("/home")
								}.onFailure { e ->
									loading = false
									if ((e as AppwriteException).type == "user_invalid_credentials") {
										passwordError = "Incorrect password"
										return@onFailure
									}
									e.printStackTrace()
								}
							}
						},
						shape = CircleShape,
					) {
						Text("Login")
					}
				} else {
					Button(
						onClick = {
							if (!Validator.isValidEmail(email)) {
								emailError = "Invalid email"
							}
							if (!Validator.isValidPassword(password)) {
								passwordError =
									"Password must have:\n - 1 special character\n - 1 uppercase letter\n - 1 number\nand be at least 10 characters long"
							}
							if (listOf(emailError, passwordError).fastAny { it != null }) {
								// Houston we have a problem (in email or password validity)
								return@Button
							}
							loading = true
							Appwrite.createUser(email, password) { result ->
								result.onSuccess {
									loading = false
									navigator.navigate("/home")
								}.onFailure { e ->
									loading = false
									
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
						},
						shape = CircleShape,
					) {
						Text("Sign up")
					}
				}
				Box(modifier = Modifier.height(16.dp))
				if (loading) {
					CircularProgressIndicator(
						modifier = Modifier
							.width(64.dp)
					)
				}
			}
		}
	}
}