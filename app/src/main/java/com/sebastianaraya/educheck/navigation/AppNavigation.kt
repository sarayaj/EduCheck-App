package com.sebastianaraya.educheck.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sebastianaraya.educheck.ui.screens.*
import com.sebastianaraya.educheck.viewmodel.TeacherViewModel
import com.sebastianaraya.educheck.viewmodel.AttendanceViewModel

/**
 * 💡 AppNavigation.kt — versión final MVVM
 * Controlador central de rutas de la aplicación EduCheck.
 * Gestiona toda la navegación entre pantallas usando Jetpack Compose Navigation.
 * Recibe los ViewModels desde MainActivity para compartir datos y lógica.
 */
@Composable
fun AppNavigation(
    teacherViewModel: TeacherViewModel,
    attendanceViewModel: AttendanceViewModel
) {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // 🔹 Pantalla de inicio de sesión
        composable("login") {
            LoginScreen(
                navController = navController,
                teacherViewModel = teacherViewModel
            )
        }

        // 🔹 Registro de docentes
        composable("register") {
            RegisterScreen(
                navController = navController,
                teacherViewModel = teacherViewModel
            )
        }

        // 🔹 Menú principal
        composable("home") {
            HomeScreen(navController = navController)
        }

        // 🔹 Registro de asistencia (usa AttendanceViewModel)
        composable("asistencia") {
            AttendanceScreen(
                navController = navController,
                attendanceViewModel = attendanceViewModel
            )
        }

        // 🔹 Lista de docentes
        composable("teacher_list") {
            TeacherListScreen(
                navController = navController,
                teacherViewModel = teacherViewModel
            )
        }

        // 🔹 Lista de asistencias registradas
        composable("attendance_list") {
            AttendanceListScreen(navController = navController)
        }

        // 🔹 Perfil de usuario
        composable("profile") {
            ProfileScreen(
                navController = navController,
                teacherViewModel = teacherViewModel
            )
        }
    }
}
