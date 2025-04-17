package com.example.financesmanagementapp.ui.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.financesmanagementapp.R
import com.example.financesmanagementapp.navigation.AppScreens
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController : NavController,
    viewModel: LoginViewModel
) {
    Box(
        Modifier
            .fillMaxSize()
            .imePadding()
            .padding(16.dp)
    ) {
        Login(Modifier.align(Alignment.Center), viewModel, navController)
    }
}

@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel, navController: NavController) {
    val user by viewModel.user.collectAsState(initial = "")
    val password by viewModel.password.collectAsState(initial = "")
    val loginEnabled by viewModel.loginEnabled.collectAsState(initial = false)
    val isLoading by viewModel.isLoading.collectAsState(initial = false)
    val coroutineScope = rememberCoroutineScope() // Creates a coroutine scope to launch async tasks without blocking the main thread
    val context = LocalContext.current

    if(isLoading){
        Box(Modifier.fillMaxSize()){
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        Column(modifier = modifier) { // Para poder tener un modifier que alinee es necesario estar dentro de un Box
            HeaderImage(Modifier.align(Alignment.CenterHorizontally)) // Se usa Modifier que es el modificador de la columna, no el modifier recibido desde fuera
            Spacer(modifier = Modifier.padding(16.dp))
            UserField(user) {
                viewModel.onLoginDataChanged(
                    it,
                    password
                )
            } // Se recibe tanto el ultimo estado del user como de la password
            PasswordField(password) { viewModel.onLoginDataChanged(user, it) }
            Spacer(modifier = Modifier.padding(8.dp))
            ForgotPassword(modifier = Modifier.align(Alignment.End))
            Spacer(modifier = Modifier.padding(16.dp))
            LoginButton(loginEnabled) {
                coroutineScope.launch {
                    viewModel.onLoginButtonClicked(context)
                    navController.navigate(route = AppScreens.HomeStartScreen.route)
                }
            }
        }
    }
}

@Composable
fun LoginButton(
    loginEnabled: Boolean,
    onLoginButtonClicked: () -> Unit
) { // Principle: single source of truth
    Button(
        onClick = {
            onLoginButtonClicked()
        },
        Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF005FD4),
            disabledContainerColor = Color.LightGray
        ),
        enabled = loginEnabled
    ) {
        Text("Ingresar", color = Color.White)
    }
}

@Composable
fun ForgotPassword(modifier: Modifier) {
    Text(
        text = "Olvidaste la contraseña?",
        modifier = modifier.clickable { },
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF3F51B5)
    )
}

@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.login_image),
        contentDescription = "LoginImage",
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserField(user: String, onTextFieldChanged: (String) -> Unit) {

    TextField(
        value = user,
        onValueChange = { newText -> onTextFieldChanged(newText) }, // Podria haber sido it
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Ingrese usuario") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            focusedTextColor = Color.Black,
            containerColor = Color(0xFF00BCD4)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        value = password,
        onValueChange = onTextFieldChanged, // Es posible asignarle directamente onTextFieldChanged, los tipos coinciden
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Ingrese contraseña") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            focusedTextColor = Color.Black,
            containerColor = Color(0xFF00BCD4)
        )
    )
}