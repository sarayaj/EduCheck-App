package com.sebastianaraya.educheck.ui.screens
// Pantalla que muestra los docentes guardados en Room, conectada al TeacherViewModel.

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sebastianaraya.educheck.data.local.TeacherEntity
import com.sebastianaraya.educheck.viewmodel.TeacherViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherListScreen(
    navController: NavController,
    teacherViewModel: TeacherViewModel
) {
    val scope = rememberCoroutineScope()

    val teachers by teacherViewModel.teachers.collectAsState() // Lista de docentes
    val isLoading by teacherViewModel.isLoading.collectAsState() // Estado de carga

    // Fondo con degradado azul
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF000428), Color(0xFF004E92))
    )

    // Cargar docentes al iniciar la pantalla
    LaunchedEffect(Unit) {
        teacherViewModel.loadTeachers()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título principal
            Text(
                text = "Docentes Registrados",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFF1E90FF).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 10.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Lista de usuarios almacenados en Room (vía ViewModel)",
                color = Color(0xFFB0C4DE),
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp),
                textAlign = TextAlign.Center
            )

            // Mostrar estado según carga o lista vacía
            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else if (teachers.isEmpty()) {
                Text(
                    text = "No hay docentes registrados.",
                    color = Color(0xFFB0C4DE),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(teachers) { teacher ->
                        TeacherCard(teacher = teacher) // Muestra cada docente
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para volver al menú
            Button(
                onClick = { navController.navigate("home") },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E90FF))
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Volver al menú", color = Color.White)
            }
        }
    }
}

// Tarjeta con datos del docente (nombre y correo)
@Composable
fun TeacherCard(teacher: TeacherEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE6EEFF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = Color(0xFF1E90FF),
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = teacher.nombre,
                    color = Color(0xFF0A1931),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = teacher.correo,
                    color = Color(0xFF334466),
                    fontSize = 14.sp
                )
            }
        }
    }
}

// Recordatorio: esta pantalla solo muestra datos ya guardados y permite volver al menú principal.
