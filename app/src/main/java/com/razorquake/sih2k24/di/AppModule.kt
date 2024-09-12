package com.razorquake.sih2k24.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.razorquake.sih2k24.data.repository.AppRepositoryImpl
import com.razorquake.sih2k24.data.local.AppDatabase
import com.razorquake.sih2k24.data.local.LocalDateTimeConvertor
import com.razorquake.sih2k24.data.local.SpeechLogDao
import com.razorquake.sih2k24.data.repository.AuthRepositoryImpl
import com.razorquake.sih2k24.data.repository.UserRepositoryImpl
import com.razorquake.sih2k24.domain.repository.AppRepository
import com.razorquake.sih2k24.domain.repository.AuthRepository
import com.razorquake.sih2k24.domain.repository.UserRepository
import com.razorquake.sih2k24.domain.usecases.AppUseCases
import com.razorquake.sih2k24.domain.usecases.DeleteSpeechLog
import com.razorquake.sih2k24.domain.usecases.GetAllSpeechLog
import com.razorquake.sih2k24.domain.usecases.GetSpeechLog
import com.razorquake.sih2k24.domain.usecases.InsertSpeechLog
import com.razorquake.sih2k24.domain.usecases.auth.AuthUseCases
import com.razorquake.sih2k24.domain.usecases.auth.GetAuthStateUseCase
import com.razorquake.sih2k24.domain.usecases.auth.LoginUseCase
import com.razorquake.sih2k24.domain.usecases.auth.LogoutUseCase
import com.razorquake.sih2k24.domain.usecases.auth.SignUpUseCase
import com.razorquake.sih2k24.domain.usecases.user.GetUserInfoUseCase
import com.razorquake.sih2k24.domain.usecases.user.UpdateUserInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")


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

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.dataStore

    @Provides
    @Singleton
    fun provideAuthRepository(dataStore: DataStore<Preferences>): AuthRepository = AuthRepositoryImpl(dataStore)

    @Provides
    @Singleton
    fun provideUserRepository(firestore: FirebaseFirestore): UserRepository = UserRepositoryImpl(firestore)


    @Provides
    @Singleton
    fun provideAuthUseCases(
        authRepository: AuthRepository,
        userRepository: UserRepository,
        auth: FirebaseAuth
    ) = AuthUseCases(
        loginUseCase = LoginUseCase(authRepository, auth),
        signUpUseCase = SignUpUseCase(authRepository, userRepository, auth),
        logoutUseCase = LogoutUseCase(authRepository, auth),
        getAuthStateUseCase = GetAuthStateUseCase(authRepository)
    )

    @Provides
    @Singleton
    fun provideGetUserInfoUseCase(userRepository: UserRepository): GetUserInfoUseCase =
        GetUserInfoUseCase(userRepository)

    @Provides
    @Singleton
    fun provideUpdateUserInfoUseCase(userRepository: UserRepository): UpdateUserInfoUseCase =
        UpdateUserInfoUseCase(userRepository)
}