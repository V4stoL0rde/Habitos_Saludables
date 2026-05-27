package com.example.habitos_saludables.data.repository

import com.example.habitos_saludables.data.remote.ApiService
import com.example.habitos_saludables.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : WeatherRepository {

    override suspend fun getCurrentTemperature(): String {
        return try {
            val response = apiService.getCurrentWeather()
            "${response.currentWeather.temperature}°C"
        } catch (e: Exception) {
            "--°C" // Si falla el internet, muestra esto
        }
    }
}