package com.sebastianaraya.educheck.data.local
// Base de datos principal de Room: une todas las entidades y DAOs.

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TeacherEntity::class, AttendanceEntity::class],
    version = 7,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    // Acceso a los DAOs
    abstract fun teacherDao(): TeacherDao
    abstract fun attendanceDao(): AttendanceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Devuelve una sola instancia de la base (Singleton)
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "educheck_db"
                )
                    .fallbackToDestructiveMigration() // Reinicia si cambia la estructura
                    .setJournalMode(RoomDatabase.JournalMode.TRUNCATE) // Optimiza rendimiento
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}

// Recordatorio: esta clase conecta las entidades con sus DAOs y crea la base de datos local.
