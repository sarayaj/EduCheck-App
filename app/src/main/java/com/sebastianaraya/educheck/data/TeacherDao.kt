package com.sebastianaraya.educheck.data.local
// DAO de Room: define las consultas SQL y operaciones CRUD para docentes.

import androidx.room.*

@Dao
interface TeacherDao {

    // Obtiene todos los docentes (orden descendente por ID)
    @Query("SELECT * FROM teacher_table ORDER BY id DESC")
    suspend fun getAllTeachers(): List<TeacherEntity>

    // Inserta o reemplaza un docente
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(teacher: TeacherEntity)

    // Actualiza todos los datos de un docente
    @Update
    suspend fun updateTeacher(teacher: TeacherEntity)

    // Actualiza solo el RUT según nombre
    @Query("UPDATE teacher_table SET rut = :rut WHERE nombre = :nombre")
    suspend fun updateRut(nombre: String, rut: String)

    // Elimina todos los registros
    @Query("DELETE FROM teacher_table")
    suspend fun deleteAll()

    // Verifica credenciales (correo y contraseña)
    @Query("SELECT * FROM teacher_table WHERE correo = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): TeacherEntity?
}

// Recordatorio: este DAO conecta la base de datos con el ViewModel a través del repositorio.
