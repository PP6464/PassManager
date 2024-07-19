package pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import backend.Appwrite
import backend.Password
import backend.Validator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.Navigator
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import passmanager.composeapp.generated.resources.Res
import passmanager.composeapp.generated.resources.ic_back
import passmanager.composeapp.generated.resources.ic_copy
import passmanager.composeapp.generated.resources.ic_lock
import passmanager.composeapp.generated.resources.ic_visibility
import passmanager.composeapp.generated.resources.ic_visibility_off
import passmanager.composeapp.generated.resources.mont

class ManagePasswordViewModel(private val id: String) {
	private val _password = MutableStateFlow<Password?>(null)
	val password = _password.asStateFlow()
	
	init {
		getPassword()
	}

	private fun getPassword() {
		CoroutineScope(Dispatchers.Default).launch {
			Appwrite.getPassword(id) { res ->
				res.onSuccess {
					_password.value = it
				}.onFailure { e ->
					e.printStackTrace()
				}
			}
		}
	}
}

@Composable
fun ManagePasswordPage(id: String, navigator: Navigator, viewModel: ManagePasswordViewModel = remember { ManagePasswordViewModel(id) }) {
	val montserrat = FontFamily(Font(Res.font.mont))
	val password = viewModel.password.collectAsState()
	var passwordError: String? by remember { mutableStateOf(null) }
	var obscureNewPassword by remember { mutableStateOf(true) }
	var obscureOldPassword by remember { mutableStateOf(true) }
	val clipboardManager = LocalClipboardManager.current
	var newPassword by remember { mutableStateOf("") }
	
	Column(
		horizontalAlignment = Alignment.Start,
		modifier = Modifier
			.padding(16.dp)
			.fillMaxSize(),
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
		if (password.value != null) {
			Text(
				text = password.value!!.domain,
				style = TextStyle(
					fontWeight = FontWeight.Bold,
					fontFamily = montserrat,
					fontSize = 30.sp,
				),
			)
			Box(modifier = Modifier.height(24.dp))
			Text(
				text = "Old password",
				style = TextStyle(
					fontWeight = FontWeight.Bold,
					fontFamily = montserrat,
					fontSize = 20.sp,
				),
			)
			Row(
				horizontalArrangement = Arrangement.Center,
				verticalAlignment = Alignment.CenterVertically,
			) {
				Text(
					text = if (obscureOldPassword) "*".repeat(password.value!!.password.length) else password.value!!.password
				)
				Box(modifier = Modifier.width(8.dp))
				IconButton(
					onClick = {
						obscureOldPassword = !obscureOldPassword
					}
				) {
					Icon(
						painter = if (obscureOldPassword) painterResource(Res.drawable.ic_visibility) else painterResource(Res.drawable.ic_visibility_off),
						contentDescription = null,
					)
				}
				Box(modifier = Modifier.width(8.dp))
				IconButton(
					onClick = {
						clipboardManager.setText(AnnotatedString(password.value!!.password))
					}
				) {
					Icon(
						painter = painterResource(Res.drawable.ic_copy),
						contentDescription = null,
					)
				}
			}
			Box(modifier = Modifier.height(16.dp))
			Text(
				text = "New password",
				style = TextStyle(
					fontFamily = montserrat,
					fontWeight = FontWeight.Bold,
					fontSize = 20.sp,
				),
			)
			Box(modifier = Modifier.height(8.dp))
			OutlinedTextField(
				value = newPassword,
				onValueChange = {
					passwordError = null
					newPassword = it
				},
				colors = OutlinedTextFieldDefaults.colors(
					errorBorderColor = Color.Red,
					focusedBorderColor = Color.Black,
					cursorColor = Color.Black,
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
						androidx.compose.material.Text(text = passwordError!!, color = Color.Red)
					}
				},
				placeholder = { androidx.compose.material.Text(text = "Password", color = Color.Gray) },
				leadingIcon = {
					androidx.compose.material.Icon(
						painter = painterResource(Res.drawable.ic_lock),
						contentDescription = null,
					)
				},
				trailingIcon = {
					androidx.compose.material.IconButton(
						onClick = {
							obscureNewPassword = !obscureNewPassword
						},
					) {
						androidx.compose.material.Icon(
							painter = if (obscureNewPassword) {
								painterResource(Res.drawable.ic_visibility)
							} else {
								painterResource(Res.drawable.ic_visibility_off)
							},
							contentDescription = null,
						)
					}
				},
				maxLines = 1,
				visualTransformation = if (obscureNewPassword) {
					PasswordVisualTransformation()
				} else {
					VisualTransformation.None
				},
				modifier = Modifier
					.width(400.dp)
			)
			Button(
				onClick = {
					if (!Validator.isValidPassword(newPassword)) {
						passwordError =
							"Password must have:\n - 1 special character\n - 1 uppercase letter\n - 1 number\nand be at least 10 characters long"
						return@Button
					}
					
					CoroutineScope(Dispatchers.IO).launch {
						Appwrite.setPassword(password.value!!.id, newPassword) { res ->
							res.onSuccess {
								navigator.goBack()
							}.onFailure { e ->
								e.printStackTrace()
							}
						}
					}
				}
			) {
				Text(
					text = "Save changes",
					style = TextStyle(
						fontFamily = montserrat,
					),
				)
			}
		} else {
			CircularProgressIndicator()
		}
	}
}