package com.sebastianaraya.educheck.data.repository

import android.content.Context
import com.sebastianaraya.educheck.data.local.AppDatabase
import com.sebastianaraya.educheck.data.local.TeacherEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 💡 TeacherRepository.kt
 * Capa de acceso a datos para la entidad Teacher.
 * Centraliza las operaciones CRUD sobre la base de datos Room
 * y expone funciones seguras para el ViewModel.
 */
class TeacherRepository(private val context: Context) {

    private val teacherDao = AppDatabase.getDatabase(context).teacherDao()

    // 📘 Obtener todos los docentes
    suspend fun getAllTeachers(): List<TeacherEntity> = withContext(Dispatchers.IO) {
        teacherDao.getAllTeachers()
    }

    // ➕ Insertar o reemplazar un docente
    suspend fun insertTeacher(teacher: TeacherEntity) = withContext(Dispatchers.IO) {
        teacherDao.insertTeacher(teacher)
    }

    // 🔁 Actualizar los datos completos de un docente
    suspend fun updateTeacher(teacher: TeacherEntity) = withContext(Dispatchers.IO) {
        teacherDao.updateTeacher(teacher)
    }

    // 🧾 Actualizar solo el RUT de un docente por nombre
    suspend fun updateRut(nombre: String, rut: String) = withContext(Dispatchers.IO) {
        teacherDao.updateRut(nombre, rut)
    }

    // 🔍 Buscar docente por login (correo y contraseña)
    suspend fun loginUser(email: String, password: String): TeacherEntity? = withContext(Dispatchers.IO) {
        teacherDao.login(email, password)
    }

    // 🗑️ Eliminar todos los docentes
    suspend fun deleteAllTeachers() = withContext(Dispatchers.IO) {
        teacherDao.deleteAll()
    }
}
