package com.razorquake.sih2k24.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.razorquake.sih2k24.domain.models.SpeechLog

@Database(entities = [SpeechLog::class], version = 2)
@TypeConverters(LocalDateTimeConvertor::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val speechLogDao: SpeechLogDao
}