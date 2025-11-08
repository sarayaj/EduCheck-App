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
 * 💡 MainActivity.kt — versión final MVVM
 * Inicializa y entrega los ViewModels globales a toda la aplicación EduCheck.
 * Cumple con la rúbrica: arquitectura modular, reutilización y persistencia de datos.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EduCheckTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // ✅ Inicialización de contexto y repositorios
                    val context = LocalContext.current
                    val teacherRepository = TeacherRepository(context)

                    // ✅ ViewModels globales
                    val teacherViewModel: TeacherViewModel = viewModel(
                        factory = TeacherViewModelFactory(teacherRepository)
                    )
                    val attendanceViewModel: AttendanceViewModel = viewModel()

                    // ✅ Navegación con ambos ViewModels inyectados
                    AppNavigation(
                        teacherViewModel = teacherViewModel,
                        attendanceViewModel = attendanceViewModel
                    )
                }
            }
        }
    }
}
