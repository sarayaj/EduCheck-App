package com.sebastianaraya.educheck.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 💡 TeacherEntity.kt
 * Representa la tabla "teacher_table" en la base de datos Room.
 * Se usa en el patrón MVVM para almacenar y recuperar docentes registrados.
 *
 * 🧱 Campos:
 * - id: Identificador único autogenerado.
 * - nombre: Nombre completo del docente.
 * - correo: Correo institucional o de acceso.
 * - password: Contraseña de ingreso.
 * - rut: Identificador opcional (puede agregarse o actualizarse posteriormente).
 */
@Entity(tableName = "teacher_table")
data class TeacherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,

    val correo: String,

    val password: String,

    val rut: String? = null // Campo opcional (permite null para registro inicial)
)
