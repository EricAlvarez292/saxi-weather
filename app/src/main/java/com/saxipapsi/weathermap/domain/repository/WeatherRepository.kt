package com.saxipapsi.weathermap.domain.repository

import com.saxipapsi.weathermap.data.remote.dto.ForecastWeatherDto
import com.saxipapsi.weathermap.data.remote.dto.RealtimeWeatherDto

interface WeatherRepository {
    suspend fun getRealtimeWeather(data: String): RealtimeWeatherDto
    suspend fun getForecastWeather(data: String, day: Int): ForecastWeatherDto
}