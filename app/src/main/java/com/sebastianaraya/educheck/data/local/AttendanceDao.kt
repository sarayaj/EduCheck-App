package com.sebastianaraya.educheck.data.local
// DAO de Room: maneja las operaciones CRUD de la tabla de asistencias.

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AttendanceDao {

    // Inserta un nuevo registro de asistencia
    @Insert
    suspend fun insertAttendance(attendance: AttendanceEntity)

    // Obtiene todas las asistencias (ordenadas por ID descendente)
    @Query("SELECT * FROM attendance_table ORDER BY id DESC")
    suspend fun getAllAttendances(): List<AttendanceEntity>

    // Elimina todos los registros de asistencia
    @Query("DELETE FROM attendance_table")
    suspend fun deleteAllAttendances()
}

// Recordatorio: este DAO conecta la app con la tabla "attendance_table" para guardar y leer asistencias.
