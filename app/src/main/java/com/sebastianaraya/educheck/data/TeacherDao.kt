package com.sebastianaraya.educheck.data.local

import androidx.room.*

/**
 * 💡 TeacherDao.kt
 * Interfaz de acceso a datos (DAO) para la tabla "teacher_table".
 * Define todas las operaciones CRUD y consultas personalizadas
 * usadas por el repositorio y el ViewModel.
 */
@Dao
interface TeacherDao {

    // 🔹 Obtener todos los docentes ordenados (más reciente primero)
    @Query("SELECT * FROM teacher_table ORDER BY id DESC")
    suspend fun getAllTeachers(): List<TeacherEntity>

    // 🔹 Insertar o reemplazar un docente
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(teacher: TeacherEntity)

    // 🔹 Actualizar los datos completos de un docente
    @Update
    suspend fun updateTeacher(teacher: TeacherEntity)

    // 🔹 Actualizar solo el RUT del docente según su nombre
    @Query("UPDATE teacher_table SET rut = :rut WHERE nombre = :nombre")
    suspend fun updateRut(nombre: String, rut: String)

    // 🔹 Eliminar todos los registros
    @Query("DELETE FROM teacher_table")
    suspend fun deleteAll()

    // 🔹 Login validando correo y contraseña
    @Query("SELECT * FROM teacher_table WHERE correo = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): TeacherEntity?
}
