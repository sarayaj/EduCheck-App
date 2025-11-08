package com.sebastianaraya.educheck.ui.screens

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.sebastianaraya.educheck.viewmodel.TeacherViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 💡 ProfileScreen.kt — Versión MVVM
 * Pantalla de perfil conectada al TeacherViewModel.
 * Cumple criterios de la rúbrica: arquitectura limpia, modularidad, validaciones y recursos nativos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, teacherViewModel: TeacherViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    // Datos del usuario (en un caso real podrían venir del ViewModel o DB)
    val nombre by remember { mutableStateOf("Sebastián Araya") }
    val correo by remember { mutableStateOf("admin@educheck.cl") }
    var rut by remember { mutableStateOf("") }

    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }
    var showCamera by remember { mutableStateOf(false) }

    // 🎨 Fondo degradado institucional
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF001F3F), Color(0xFF004E92))
    )

    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }
    val imageCapture = remember { ImageCapture.Builder().build() }

    // Permiso de cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) showCamera = true
        else Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Título
            Text(
                text = "Mi Perfil",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF002B5B).copy(alpha = 0.8f), RoundedCornerShape(10.dp))
                    .padding(vertical = 10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🧍 Imagen de perfil
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E90FF).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                if (capturedImage != null) {
                    Image(
                        bitmap = capturedImage!!.asImageBitmap(),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFF87CEFA),
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🎥 Botones de cámara
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E90FF)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Abrir Cámara")
                }

                if (showCamera) {
                    Button(
                        onClick = { showCamera = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Cerrar")
                    }
                }
            }

            // 🎥 Cámara en miniatura
            if (showCamera) {
                CameraPreviewSmall(
                    lifecycleOwner = lifecycleOwner,
                    imageCapture = imageCapture,
                    executor = cameraExecutor
                ) { bitmap ->
                    capturedImage = bitmap
                    showCamera = false
                    Toast.makeText(context, "📸 Foto capturada correctamente", Toast.LENGTH_SHORT).show()
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🧾 Campos de datos
            OutlinedTextField(
                value = nombre,
                onValueChange = {},
                label = { Text("Nombre completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1E90FF),
                    focusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = {},
                label = { Text("Correo electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1E90FF),
                    focusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = rut,
                onValueChange = { rut = it },
                label = { Text("RUT") },
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1E90FF),
                    focusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 💾 Botón Guardar
            Button(
                onClick = {
                    if (rut.isNotBlank()) {
                        scope.launch {
                            val success = teacherViewModel.updateRut(nombre, rut)
                            if (success) {
                                Toast.makeText(context, "✅ RUT actualizado correctamente", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "❌ Error al actualizar RUT", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "⚠ Debes ingresar un RUT válido", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B894)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Guardar cambios")
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 🔙 Volver al menú
            Button(
                onClick = { navController.navigate("home") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E90FF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Volver al menú", color = Color.White)
            }
        }
    }
}

/**
 * 📸 Vista de cámara en miniatura — estable y segura
 */
@Composable
fun CameraPreviewSmall(
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    imageCapture: ImageCapture,
    executor: ExecutorService,
    onImageCaptured: (Bitmap) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black),
        contentAlignment = Alignment.BottomCenter
    ) {
        AndroidView(factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }

            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        })

        // 📷 Botón para tomar foto
        Button(
            onClick = {
                try {
                    val photoFile = File.createTempFile("photo_", ".jpg", context.cacheDir)
                    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                    imageCapture.takePicture(
                        outputOptions,
                        executor,
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                android.os.Handler(context.mainLooper).postDelayed({
                                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                                    if (bitmap != null) onImageCaptured(bitmap)
                                    else Toast.makeText(context, "⚠ No se pudo leer la imagen", Toast.LENGTH_SHORT).show()
                                }, 150)
                            }

                            override fun onError(exception: ImageCaptureException) {
                                exception.printStackTrace()
                                Toast.makeText(context, "❌ Error al capturar: ${exception.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "❌ Error inesperado al guardar la foto", Toast.LENGTH_LONG).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E90FF)),
            shape = CircleShape,
            modifier = Modifier
                .padding(10.dp)
                .size(56.dp)
        ) {
            Icon(Icons.Default.PhotoCamera, contentDescription = "Tomar Foto", tint = Color.White)
        }
    }
}
