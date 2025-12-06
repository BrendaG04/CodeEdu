package com.example.codeedu.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.codeedu.data.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Login screen - basic username/password
 * onLoginSuccess(role, username)
 */
@Composable
fun LoginScreen(onLoginSuccess: (String, String) -> Unit, onBack: () -> Unit) {
    val ctx = LocalContext.current
    val repo = remember { UserRepository(ctx) }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Login", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), singleLine = true, modifier = Modifier.fillMaxWidth())


        if (errorMessage.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }
        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                if (username.isBlank() || password.isBlank()) {
                    errorMessage = "Please enter username and password"
                    return@Button
                }
                CoroutineScope(Dispatchers.IO).launch {
                    val user = repo.findByUsername(username)
                    if (user == null || user.password != password) {
                        CoroutineScope(Dispatchers.Main).launch { errorMessage = "Invalid credentials" }
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(ctx, "Welcome ${user.username}", Toast.LENGTH_SHORT).show()
                            onLoginSuccess(user.role, user.username)
                        }
                    }
                }
            }) {
                Text("Login")
            }
            OutlinedButton(onClick = onBack) {
                Text("Cancel")
            }
        }
    }
}
