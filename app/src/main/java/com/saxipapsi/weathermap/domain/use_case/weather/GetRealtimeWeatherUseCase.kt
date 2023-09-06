package com.saxipapsi.weathermap.domain.use_case.weather

import android.util.Log
import com.saxipapsi.weathermap.common.Resource
import com.saxipapsi.weathermap.data.remote.dto.RealtimeWeatherDto
import com.saxipapsi.weathermap.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetRealtimeWeatherUseCase(private val repo: WeatherRepository) {
    operator fun invoke(data: String): Flow<Resource<RealtimeWeatherDto>> = flow {
        try {
            Log.d("eric", "GetRealtimeWeatherUseCase")
            emit(Resource.Loading())
            val rtWeather = repo.getRealtimeWeather(data)
            emit(Resource.Success(rtWeather))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Please check your internet connection"))
        }
    }
}