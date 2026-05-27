package com.example.habitos_saludables.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // Coordenadas de Puerto Montt por defecto
    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") lat: Double = -41.4693,
        @Query("longitude") lon: Double = -72.9424,
        @Query("current_weather") currentWeather: Boolean = true
    ): WeatherResponse
}
