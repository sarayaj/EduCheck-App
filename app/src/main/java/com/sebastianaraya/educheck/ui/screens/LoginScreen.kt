package com.sebastianaraya.educheck.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sebastianaraya.educheck.viewmodel.TeacherViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 💡 LoginScreen.kt — Versión final sincronizada con el ViewModel
 * Usa loginUser() que devuelve TeacherEntity? y valida correctamente el acceso.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    teacherViewModel: TeacherViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // 🧠 Estados locales
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }

    // 🌐 Estados globales del ViewModel
    val isLoading by teacherViewModel.isLoading.collectAsState()
    val formError by teacherViewModel.formError.collectAsState()
    val loginError by teacherViewModel.loginError.collectAsState()

    val isValid = correo.contains("@") && password.isNotBlank()

    // ✨ Animación de aparición
    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true
    }

    // 🎨 Fondo con gradiente institucional
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF000428), Color(0xFF004E92))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
    ) {
        // 🔵 Círculos decorativos
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.05f),
                radius = 400f,
                center = Offset(x = size.width * 0.8f, y = size.height * 0.2f)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.07f),
                radius = 250f,
                center = Offset(x = size.width * 0.2f, y = size.height * 0.8f)
            )
        }

        // 🎬 Contenido principal animado
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
                    .padding(top = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 🏷️ Título
                Text(
                    text = "EduCheck",
                    style = TextStyle(
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Inicio de Sesión",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color(0xFFB0C4DE),
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(bottom = 40.dp)
                )

                // 🧾 Correo
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo electrónico", color = Color.LightGray) },
                    isError = showErrors && !correo.contains("@"),
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null, tint = Color.LightGray)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors(),
                    supportingText = {
                        if (showErrors && !correo.contains("@")) {
                            Text("Correo inválido", color = Color(0xFFFF6B6B))
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 🔒 Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña", color = Color.LightGray) },
                    isError = showErrors && password.isBlank(),
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color.LightGray)
                    },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default,
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors(),
                    supportingText = {
                        if (showErrors && password.isBlank()) {
                            Text("Campo obligatorio", color = Color(0xFFFF6B6B))
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 🔘 Botón de ingreso
                Button(
                    onClick = {
                        showErrors = true
                        if (isValid) {
                            scope.launch {
                                teacherViewModel.loadTeachers()
                                val user = teacherViewModel.loginUser(correo, password)

                                if (user != null) {
                                    Toast.makeText(
                                        context,
                                        "Bienvenido ${user.nombre} 👋",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate("home")
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Credenciales incorrectas ❌",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1E90FF),
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(8.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Ingresar", fontWeight = FontWeight.SemiBold)
                    }
                }

                // ❗ Error general
                if (!formError.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(formError!!, color = Color(0xFFFF6B6B), fontSize = 14.sp)
                }

                if (!loginError.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(loginError!!, color = Color(0xFFFF6B6B), fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 🆕 Enlace de registro
                TextButton(onClick = { navController.navigate("register") }) {
                    Text(
                        text = "¿No tienes cuenta? Regístrate aquí",
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

/**
 * 🎨 Paleta de colores unificada para TextFields
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun textFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color(0x33102040),
        unfocusedContainerColor = Color(0x33102040),
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedBorderColor = Color(0xFF1E90FF),
        unfocusedBorderColor = Color(0xFF334466),
        cursorColor = Color.White,
        focusedLabelColor = Color(0xFF1E90FF),
        unfocusedLabelColor = Color.LightGray,
        focusedLeadingIconColor = Color.White,
        unfocusedLeadingIconColor = Color.LightGray
    )
}
