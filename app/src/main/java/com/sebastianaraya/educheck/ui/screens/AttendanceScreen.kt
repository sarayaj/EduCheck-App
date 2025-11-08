package com.sebastianaraya.educheck.ui.screens

import android.Manifest
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.sebastianaraya.educheck.viewmodel.AttendanceViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 💡 AttendanceScreen.kt — Versión MVVM
 * Registra la asistencia de estudiantes con soporte para cámara simulada.
 * Toda la lógica de persistencia y validación se delega al AttendanceViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    navController: NavController,
    attendanceViewModel: AttendanceViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    // Estados locales
    var scanning by remember { mutableStateOf(false) }
    val cameraPermissionGranted = remember { mutableStateOf(false) }

    // Estados del ViewModel (reactivos)
    val nombre by attendanceViewModel.nombre.collectAsState()
    val rut by attendanceViewModel.rut.collectAsState()
    val mensaje by attendanceViewModel.mensaje.collectAsState()
    val isLoading by attendanceViewModel.isLoading.collectAsState()

    // Lanzador de permisos
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        cameraPermissionGranted.value = granted
        if (!granted)
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
    }

    // 🎨 Fondo institucional
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF000428), Color(0xFF004E92))
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔷 Título
            Text(
                text = "Registrar Asistencia",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E90FF).copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                    .padding(vertical = 10.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🧍 Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { attendanceViewModel.actualizarNombre(it) },
                label = { Text("Nombre del estudiante", color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.LightGray) },
                singleLine = true,
                colors = textFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 🪪 RUT
            OutlinedTextField(
                value = rut,
                onValueChange = { attendanceViewModel.actualizarRut(it) },
                label = { Text("RUT del estudiante", color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = Color.LightGray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                colors = textFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 🔘 Botón principal
            Button(
                onClick = {
                    scope.launch {
                        attendanceViewModel.registrarAsistencia(context)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E90FF))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text("Registrar", color = Color.White)
                }
            }

            if (mensaje.isNotEmpty()) {
                Text(
                    text = mensaje,
                    color = if (mensaje.contains("✅")) Color(0xFF00FA9A) else Color(0xFFFF6B6B),
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Divider(
                color = Color(0xFF334466),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // 📷 Escaneo simulado
            Text("O escanear carnet (simulación):", color = Color.White, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        if (!cameraPermissionGranted.value) {
                            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                        } else {
                            scanning = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4169E1)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Escanear Carnet")
                }

                if (scanning) {
                    TextButton(onClick = { scanning = false }) {
                        Text("Cerrar", color = Color.LightGray)
                    }
                }
            }

            AnimatedVisibility(visible = scanning, enter = fadeIn(), exit = fadeOut()) {
                CameraSimulationSection(
                    cameraPermissionGranted = cameraPermissionGranted.value,
                    lifecycleOwner = lifecycleOwner,
                    onSimulateCapture = { attendanceViewModel.simularCaptura() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = { navController.navigate("attendance_list") }) {
                Text("📋 Ver Asistencias Registradas", color = Color(0xFF87CEFA))
            }

            TextButton(onClick = { navController.navigate("home") }) {
                Text("⬅️ Volver al menú", color = Color.LightGray)
            }
        }
    }
}

/**
 * 🎥 Sección simulada de cámara
 */
@Composable
fun CameraSimulationSection(
    cameraPermissionGranted: Boolean,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    onSimulateCapture: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(140.dp)
                .background(Color.Black, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (cameraPermissionGranted) {
                CameraPreviewView(lifecycleOwner = lifecycleOwner)
            } else {
                Text("Permiso de cámara requerido", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onSimulateCapture,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B894)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.Camera, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Tomar foto (simulación)")
        }
    }
}

@Composable
fun CameraPreviewView(lifecycleOwner: androidx.lifecycle.LifecycleOwner) {
    val context = LocalContext.current
    AndroidView(factory = { ctx ->
        val previewView = PreviewView(ctx).also {
            it.scaleType = PreviewView.ScaleType.FILL_CENTER
        }
        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(previewView.surfaceProvider)
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(ctx))
        previewView
    })
}

/**
 * 🎨 Paleta de colores para campos
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun textFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color(0xFF0F2540),
        unfocusedContainerColor = Color(0xFF0F2540),
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedBorderColor = Color(0xFF1E90FF),
        unfocusedBorderColor = Color(0xFF334466),
        cursorColor = Color.White
    )
}
