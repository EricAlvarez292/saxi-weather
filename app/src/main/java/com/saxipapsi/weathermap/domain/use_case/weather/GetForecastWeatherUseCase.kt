package com.saxipapsi.weathermap.domain.use_case.weather

import com.saxipapsi.weathermap.common.Resource
import com.saxipapsi.weathermap.data.remote.dto.ForecastWeatherDto
import com.saxipapsi.weathermap.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetForecastWeatherUseCase(private val repo: WeatherRepository) {
    operator fun invoke(data: String, day: Int = 3): Flow<Resource<ForecastWeatherDto>> = flow {
        try {
            emit(Resource.Loading())
            val fcWeather = repo.getForecastWeather(data, day)
            emit(Resource.Success(fcWeather))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Please check your internet connection"))
        }
    }
}