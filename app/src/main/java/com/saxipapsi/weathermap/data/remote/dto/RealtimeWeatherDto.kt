package com.saxipapsi.weathermap.data.remote.dto

data class RealtimeWeatherDto(
    val current: CurrentDto,
    val location: LocationDto
)