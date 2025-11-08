package com.sebastianaraya.educheck.ui.screens
// Pantalla principal del menú de inicio de la app. Conecta a todas las secciones.

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var isVisible by remember { mutableStateOf(false) }

    // Animación de entrada
    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true
    }

    // Fondo azul degradado
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF004E92), Color(0xFF000428))
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                // Título de bienvenida
                Text(
                    text = "Bienvenido a EduCheck",
                    style = TextStyle(
                        color = Color(0xFFBFD7ED),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Selecciona una opción para comenzar",
                    color = Color(0xFFB0C4DE),
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Botones del menú principal
                MainMenuButton(
                    text = "Registrar Asistencia",
                    icon = Icons.Default.CheckCircle,
                    onClick = { navController.navigate("asistencia") }
                )

                MainMenuButton(
                    text = "Ver Estudiantes (Asistencias)",
                    icon = Icons.Default.ListAlt,
                    onClick = { navController.navigate("attendance_list") }
                )

                MainMenuButton(
                    text = "Ver Docentes Registrados",
                    icon = Icons.Default.Group,
                    onClick = { navController.navigate("teacher_list") }
                )

                MainMenuButton(
                    text = "Mi Perfil",
                    icon = Icons.Default.Person,
                    onClick = { navController.navigate("profile") }
                )

                Spacer(modifier = Modifier.weight(1f))

                // Pie de pantalla
                Text(
                    text = "Versión 1.0 - Duoc UC",
                    color = Color.LightGray.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }
}

// Componente reutilizable de botón del menú principal
@Composable
fun MainMenuButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .height(60.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1E90FF),
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(8.dp)
    ) {
        Icon(
            icon,
            contentDescription = text,
            tint = Color.White,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

// Recordatorio: esta pantalla sirve como punto central de navegación de toda la app.
