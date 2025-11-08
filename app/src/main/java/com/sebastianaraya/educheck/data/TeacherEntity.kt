package com.sebastianaraya.educheck.data.local
// Entidad de Room que define la tabla de docentes registrados.

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teacher_table")
data class TeacherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,         // ID autogenerado

    val nombre: String,      // Nombre del docente
    val correo: String,      // Correo de acceso
    val password: String,    // Contraseña del docente
    val rut: String? = null  // Campo opcional, editable luego
)

// Recordatorio: esta entidad representa una fila en la tabla "teacher_table" de Room.
