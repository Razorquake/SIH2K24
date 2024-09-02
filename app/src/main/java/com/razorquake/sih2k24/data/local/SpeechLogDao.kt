package com.razorquake.sih2k24.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.razorquake.sih2k24.domain.SpeechLog
import kotlinx.coroutines.flow.Flow

@Dao
interface SpeechLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpeechLog(speechLog: SpeechLog)
    @Delete
    suspend fun deleteSpeechLog(speechLog: SpeechLog)
    @Query("SELECT * FROM SpeechLog")
    fun getAllSpeechLogs(): Flow<List<SpeechLog>>
    @Query("SELECT * FROM SpeechLog WHERE text = :text")
    suspend fun getSpeechLog(text: String): SpeechLog?
}