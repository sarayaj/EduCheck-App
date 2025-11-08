package com.sebastianaraya.educheck.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Base de datos principal de EduCheck
 * Incluye las entidades de docentes y asistencias
 */
@Database(
    entities = [
        TeacherEntity::class,
        AttendanceEntity::class
    ],
    version = 7, // 🔄 Aumentar siempre que cambie la estructura de las entidades
    exportSchema = true // ✅ se recomienda activarlo en producción para versionar esquemas
)
abstract class AppDatabase : RoomDatabase() {

    // 🔹 DAOs (Data Access Objects)
    abstract fun teacherDao(): TeacherDao
    abstract fun attendanceDao(): AttendanceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene una única instancia de la base de datos (patrón Singleton)
         * Evita crear múltiples conexiones a la base.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "educheck_db"
                )
                    // 🔄 Borra y recrea la base de datos si hay cambios de versión (solo en desarrollo)
                    .fallbackToDestructiveMigration()
                    // ✅ Mejora el rendimiento si accedes seguido desde el hilo principal
                    .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
