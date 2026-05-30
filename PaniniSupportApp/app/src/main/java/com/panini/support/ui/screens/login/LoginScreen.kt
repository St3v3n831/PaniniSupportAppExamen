package com.panini.support.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.panini.support.core.UiState
import com.panini.support.ui.theme.PaniniBlue
import com.panini.support.ui.theme.PaniniGold
import com.panini.support.ui.theme.PaniniRed
import com.panini.support.ui.theme.PaniniTextSec

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var email      by remember { mutableStateOf("") }
    var password   by remember { mutableStateOf("") }
    var showPass   by remember { mutableStateOf(false) }

    // Observa éxito para navegar
    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) onLoginSuccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(PaniniBlue, Color(0xFF1A237E))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape  = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier            = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo / Título
                Text(
                    text       = "⚽",
                    fontSize   = 48.sp,
                    modifier   = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text       = "Panini Support",
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color      = PaniniBlue
                )
                Text(
                    text     = "FIFA World Cup 2026",
                    fontSize = 12.sp,
                    color    = PaniniGold,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(28.dp))

                // Email
                OutlinedTextField(
                    value         = email,
                    onValueChange = { email = it },
                    label         = { Text("Correo corporativo") },
                    leadingIcon   = { Icon(Icons.Default.Email, contentDescription = null) },
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(Modifier.height(14.dp))

                // Password
                OutlinedTextField(
                    value         = password,
                    onValueChange = { password = it },
                    label         = { Text("Contraseña") },
                    leadingIcon   = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon  = {
                        IconButton(onClick = { showPass = !showPass }) {
                            Icon(
                                imageVector = if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (showPass) "Ocultar" else "Mostrar"
                            )
                        }
                    },
                    singleLine            = true,
                    visualTransformation  = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier              = Modifier.fillMaxWidth(),
                    keyboardOptions       = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                // Error message
                if (uiState is UiState.Error) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text     = (uiState as UiState.Error).message,
                        color    = PaniniRed,
                        fontSize = 13.sp
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Login button
                Button(
                    onClick  = { viewModel.login(email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled  = uiState !is UiState.Loading,
                    shape    = RoundedCornerShape(10.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = PaniniBlue)
                ) {
                    if (uiState is UiState.Loading) {
                        CircularProgressIndicator(
                            color     = Color.White,
                            modifier  = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Ingresar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text(
                    text     = "Demo: operaciones@panini.com / panini2026",
                    fontSize = 11.sp,
                    color    = PaniniTextSec
                )
            }
        }
    }
}
