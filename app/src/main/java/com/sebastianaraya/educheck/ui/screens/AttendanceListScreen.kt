package com.sebastianaraya.educheck.ui.screens
// Pantalla que muestra todas las asistencias registradas almacenadas en Room.

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sebastianaraya.educheck.data.local.AppDatabase
import com.sebastianaraya.educheck.data.local.AttendanceEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceListScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var registros by remember { mutableStateOf<List<AttendanceEntity>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }

    // Fondo institucional azul degradado
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF000428), Color(0xFF004E92))
    )

    // Carga los datos desde Room al entrar en la pantalla
    LaunchedEffect(Unit) {
        val dao = AppDatabase.getDatabase(context).attendanceDao()
        registros = dao.getAllAttendances()
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
                text = "Asistencias Registradas",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Si no hay registros
            if (registros.isEmpty()) {
                Text("No hay asistencias registradas aún.", color = Color(0xFFB0C4DE))
            } 
            // Si hay registros, muestra lista
            else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(registros) { r ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2540)),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                // Nombre del estudiante
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF1E90FF))
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = r.nombre.ifBlank { "—" },
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 17.sp
                                    )
                                }

                                Spacer(Modifier.height(4.dp))

                                // RUT
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Badge, contentDescription = null, tint = Color(0xFF1E90FF))
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = "RUT: ${r.rut.ifBlank { "—" }}",
                                        color = Color(0xFFD0E0FF),
                                        fontSize = 14.sp
                                    )
                                }

                                Spacer(Modifier.height(4.dp))

                                // Fecha y hora
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color(0xFF1E90FF))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Fecha: ${r.fecha}", color = Color(0xFFD0E0FF), fontSize = 14.sp)

                                    Spacer(Modifier.width(16.dp))

                                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color(0xFF1E90FF))
                                    Spacer(Modifier.width(6.dp))
                                    Text(r.hora, color = Color(0xFFD0E0FF), fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Botones inferiores
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Volver al menú principal
                Button(
                    onClick = { navController.navigate("home") },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E90FF))
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Volver al menú", color = Color.White)
                }

                // Botón para limpiar registros
                if (registros.isNotEmpty()) {
                    Button(
                        onClick = { showDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4C4C))
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Limpiar", color = Color.White)
                    }
                }
            }

            // Diálogo de confirmación para eliminar registros
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Confirmar acción", fontWeight = FontWeight.Bold) },
                    text = { Text("¿Deseas eliminar todas las asistencias registradas?") },
                    confirmButton = {
                        TextButton(onClick = {
                            scope.launch {
                                val dao = AppDatabase.getDatabase(context).attendanceDao()
                                dao.deleteAllAttendances()
                                registros = emptyList()
                            }
                            showDialog = false
                        }) {
                            Text("Sí, eliminar", color = Color.Red, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancelar")
                        }
                    },
                    containerColor = Color(0xFF0F2540),
                    titleContentColor = Color.White,
                    textContentColor = Color(0xFFD0E0FF)
                )
            }
        }
    }
}

// Recordatorio: esta pantalla lista todas las asistencias guardadas en Room y permite limpiarlas.
