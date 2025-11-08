package com.sebastianaraya.educheck.navigation
// Controla la navegación entre pantallas en EduCheck usando Jetpack Compose Navigation.

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sebastianaraya.educheck.ui.screens.*
import com.sebastianaraya.educheck.viewmodel.TeacherViewModel
import com.sebastianaraya.educheck.viewmodel.AttendanceViewModel

@Composable
fun AppNavigation(
    teacherViewModel: TeacherViewModel,
    attendanceViewModel: AttendanceViewModel
) {
    // Controlador principal de navegación
    val navController: NavHostController = rememberNavController()

    // Mapa de rutas de la aplicación
    NavHost(
        navController = navController,
        startDestination = "login" // Pantalla inicial
    ) {
        // Pantalla de inicio de sesión
        composable("login") {
            LoginScreen(
                navController = navController,
                teacherViewModel = teacherViewModel
            )
        }

        // Registro de docentes nuevos
        composable("register") {
            RegisterScreen(
                navController = navController,
                teacherViewModel = teacherViewModel
            )
        }

        // Menú principal (Home)
        composable("home") {
            HomeScreen(navController = navController)
        }

        // Registro de asistencia (usa ViewModel de asistencia)
        composable("asistencia") {
            AttendanceScreen(
                navController = navController,
                attendanceViewModel = attendanceViewModel
            )
        }

        // Lista de docentes registrados (Room + ViewModel)
        composable("teacher_list") {
            TeacherListScreen(
                navController = navController,
                teacherViewModel = teacherViewModel
            )
        }

        // Lista de asistencias almacenadas en Room
        composable("attendance_list") {
            AttendanceListScreen(navController = navController)
        }

        // Perfil del usuario actual
        composable("profile") {
            ProfileScreen(
                navController = navController,
                teacherViewModel = teacherViewModel
            )
        }
    }
}

// Recordatorio: este archivo gestiona todas las rutas de la app y conecta las pantallas con sus ViewModels.
