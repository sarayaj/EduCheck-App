package com.sebastianaraya.educheck


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sebastianaraya.educheck.data.repository.TeacherRepository
import com.sebastianaraya.educheck.navigation.AppNavigation
import com.sebastianaraya.educheck.ui.theme.EduCheckTheme
import com.sebastianaraya.educheck.viewmodel.TeacherViewModel
import com.sebastianaraya.educheck.viewmodel.TeacherViewModelFactory
import com.sebastianaraya.educheck.viewmodel.AttendanceViewModel

/**
 * MainActivity.kt — versión final MVVM
 * Es el punto de inicio de la app. 
 * Inicializa el tema, los ViewModels y la navegación principal.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicia la interfaz de usuario con Jetpack Compose
        setContent {
            EduCheckTheme { // Aplica el tema visual de la app
                Surface(
                    modifier = Modifier.fillMaxSize(), // Ocupa toda la pantalla
                    color = MaterialTheme.colorScheme.background // Fondo según el tema
                ) {
                    // Contexto y repositorio principal
                    val context = LocalContext.current // Permite acceder a recursos del sistema
                    val teacherRepository = TeacherRepository(context) // Maneja datos de docentes

                    // ViewModels globales
                    val teacherViewModel: TeacherViewModel = viewModel(
                        factory = TeacherViewModelFactory(teacherRepository)
                    ) // Lógica de docentes (registro/login)

                    val attendanceViewModel: AttendanceViewModel = viewModel() // Lógica de asistencia

                    // Navegación entre pantallas
                    AppNavigation(
                        teacherViewModel = teacherViewModel,
                        attendanceViewModel = attendanceViewModel
                    ) // Conecta las pantallas y mantiene los estados
                }
            }
        }
    }
}

// Recordatorio: aquí parte todo el proyecto. Se crean los ViewModels y se inicia la navegación general.
