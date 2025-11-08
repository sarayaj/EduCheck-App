package com.sebastianaraya.educheck.data.local
// Módulo auxiliar para obtener instancias del DAO sin repetir código.

import android.content.Context

object DatabaseModule {
    // Devuelve una instancia de TeacherDao lista para usar
    fun provideTeacherDao(context: Context): TeacherDao {
        val db = AppDatabase.getDatabase(context) // Obtiene la base de datos
        return db.teacherDao()                    // Retorna el DAO de docentes
    }
}

// Recordatorio: este módulo centraliza la creación de DAOs y evita duplicar instancias.

