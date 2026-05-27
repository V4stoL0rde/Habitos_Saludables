package com.example.habitos_saludables.domain.repository

interface WeatherRepository {
    suspend fun getCurrentTemperature(): String
}