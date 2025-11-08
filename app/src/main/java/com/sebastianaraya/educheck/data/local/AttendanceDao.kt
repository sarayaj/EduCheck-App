package com.sebastianaraya.educheck.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AttendanceDao {

    @Insert
    suspend fun insertAttendance(attendance: AttendanceEntity)

    @Query("SELECT * FROM attendance_table ORDER BY id DESC")
    suspend fun getAllAttendances(): List<AttendanceEntity>

    @Query("DELETE FROM attendance_table")
    suspend fun deleteAllAttendances()
}
