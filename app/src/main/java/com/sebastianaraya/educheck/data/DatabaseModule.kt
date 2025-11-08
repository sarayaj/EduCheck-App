package com.sebastianaraya.educheck.data.local

import android.content.Context

object DatabaseModule {
    fun provideTeacherDao(context: Context): TeacherDao {
        val db = AppDatabase.getDatabase(context)
        return db.teacherDao()
    }
}

