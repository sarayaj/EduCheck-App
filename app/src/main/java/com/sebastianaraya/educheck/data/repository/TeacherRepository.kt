package com.sebastianaraya.educheck.data.repository
// Repositorio que conecta el ViewModel con la base de datos (TeacherDao).

import android.content.Context
import com.sebastianaraya.educheck.data.local.AppDatabase
import com.sebastianaraya.educheck.data.local.TeacherEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TeacherRepository(private val context: Context) {

    private val teacherDao = AppDatabase.getDatabase(context).teacherDao() // Acceso al DAO

    // Obtiene todos los docentes (en hilo secundario)
    suspend fun getAllTeachers(): List<TeacherEntity> = withContext(Dispatchers.IO) {
        teacherDao.getAllTeachers()
    }

    // Inserta o reemplaza un docente
    suspend fun insertTeacher(teacher: TeacherEntity) = withContext(Dispatchers.IO) {
        teacherDao.insertTeacher(teacher)
    }

    // Actualiza todos los datos de un docente
    suspend fun updateTeacher(teacher: TeacherEntity) = withContext(Dispatchers.IO) {
        teacherDao.updateTeacher(teacher)
    }

    // Actualiza solo el RUT según nombre
    suspend fun updateRut(nombre: String, rut: String) = withContext(Dispatchers.IO) {
        teacherDao.updateRut(nombre, rut)
    }

    // Verifica login (correo + contraseña)
    suspend fun loginUser(email: String, password: String): TeacherEntity? = withContext(Dispatchers.IO) {
        teacherDao.login(email, password)
    }

    // Elimina todos los docentes
    suspend fun deleteAllTeachers() = withContext(Dispatchers.IO) {
        teacherDao.deleteAll()
    }
}

// Recordatorio: el repositorio separa la lógica de datos del ViewModel y usa corrutinas para no bloquear la UI.
