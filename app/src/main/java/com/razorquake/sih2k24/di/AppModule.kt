package com.razorquake.sih2k24.di

import android.app.Application
import androidx.room.Room
import com.razorquake.sih2k24.data.AppRepositoryImpl
import com.razorquake.sih2k24.data.local.AppDatabase
import com.razorquake.sih2k24.data.local.LocalDateTimeConvertor
import com.razorquake.sih2k24.data.local.SpeechLogDao
import com.razorquake.sih2k24.domain.repository.AppRepository
import com.razorquake.sih2k24.domain.usecases.AppUseCases
import com.razorquake.sih2k24.domain.usecases.DeleteSpeechLog
import com.razorquake.sih2k24.domain.usecases.GetAllSpeechLog
import com.razorquake.sih2k24.domain.usecases.GetSpeechLog
import com.razorquake.sih2k24.domain.usecases.InsertSpeechLog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAppRepository(
        speechLogDao: SpeechLogDao
    ): AppRepository {
        return AppRepositoryImpl(speechLogDao)
    }

    @Provides
    @Singleton
    fun provideAppUseCases(
        appRepository: AppRepository
    ) = AppUseCases(
        getAllSpeechLog = GetAllSpeechLog(
            repository = appRepository
        ),
        getSpeechLog = GetSpeechLog(
            repository = appRepository
        ),
        deleteSpeechLog = DeleteSpeechLog(
            repository = appRepository
        ),
        insertSpeechLog = InsertSpeechLog(
            repository = appRepository
        )
    )

    @Provides
    @Singleton
    fun provideAppDatabase(
        application: Application
    ): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "app_db"
        ).addTypeConverter(LocalDateTimeConvertor())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideSpeechLogDao(
        appDatabase: AppDatabase
    ): SpeechLogDao {
        return appDatabase.speechLogDao
    }
}