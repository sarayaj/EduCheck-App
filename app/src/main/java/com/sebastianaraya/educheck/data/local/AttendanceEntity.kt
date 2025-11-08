package com.sebastianaraya.educheck.data.local
// Entidad de Room que representa un registro de asistencia.

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance_table")
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID autogenerado
    val nombre: String, // Nombre del estudiante
    val rut: String,    // RUT del estudiante
    val fecha: String,  // Fecha del registro
    val hora: String    // Hora exacta del registro
)

// Recordatorio: cada objeto de esta clase equivale a una fila en la tabla "attendance_table".
