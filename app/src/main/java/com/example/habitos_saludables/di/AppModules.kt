package com.example.habitos_saludables.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.habitos_saludables.data.local.HabitDao
import com.example.habitos_saludables.data.local.HabitEntity
import com.example.habitos_saludables.data.remote.ApiService
import com.example.habitos_saludables.data.repository.HabitRepositoryImpl
import com.example.habitos_saludables.data.repository.QuoteRepositoryImpl
import com.example.habitos_saludables.data.repository.WeatherRepositoryImpl
import com.example.habitos_saludables.domain.repository.HabitRepository
import com.example.habitos_saludables.domain.repository.QuoteRepository
import com.example.habitos_saludables.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@androidx.room.Database(entities = [HabitEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}

@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "habits_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideHabitDao(db: AppDatabase): HabitDao = db.habitDao()

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/") // URL de la API
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideHabitRepository(habitDao: HabitDao): HabitRepository {
        return HabitRepositoryImpl(habitDao)
    }

    @Provides
    @Singleton
    fun provideQuoteRepository(): QuoteRepository {
        return QuoteRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(apiService: ApiService): WeatherRepository {
        return WeatherRepositoryImpl(apiService)
    }
}