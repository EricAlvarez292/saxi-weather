package com.saxipapsi.weathermap.data.remote.dto

data class ForecastdayDto(
    val astro: AstroDto,
    val date: String,
    val date_epoch: Int,
    val day: DayDto,
    val hour: List<HourDto>
)