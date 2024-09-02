package com.razorquake.sih2k24.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.razorquake.sih2k24.domain.SpeechLog

@Database(entities = [SpeechLog::class], version = 1)
@TypeConverters(LocalDateTimeConvertor::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val speechLogDao: SpeechLogDao
}