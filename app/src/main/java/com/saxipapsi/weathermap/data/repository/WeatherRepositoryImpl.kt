package com.saxipapsi.weathermap.data.repository

import com.saxipapsi.weathermap.data.remote.WeatherApi
import com.saxipapsi.weathermap.data.remote.dto.ForecastWeatherDto
import com.saxipapsi.weathermap.data.remote.dto.RealtimeWeatherDto
import com.saxipapsi.weathermap.domain.repository.WeatherRepository

class WeatherRepositoryImpl(private val api: WeatherApi) : WeatherRepository {
    override suspend fun getRealtimeWeather(data: String): RealtimeWeatherDto = api.getRealtimeWeather(data = data)
    override suspend fun getForecastWeather(data: String, day: Int): ForecastWeatherDto = api.getForecastWeather(data = data, day = day)
}