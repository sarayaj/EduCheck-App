package com.sebastianaraya.educheck.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance_table")
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val rut: String,
    val fecha: String,
    val hora: String
)
