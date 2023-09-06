package com.saxipapsi.weathermap.data.remote.dto

data class ForecastWeatherDto(
    val current: CurrentDto,
    val forecast: ForecastDto,
    val location: LocationDto
)