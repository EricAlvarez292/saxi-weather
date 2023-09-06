package com.saxipapsi.weathermap.domain.use_case.geo

import com.saxipapsi.weathermap.common.Resource
import com.saxipapsi.weathermap.data.remote.dto.ForecastWeatherDto
import com.saxipapsi.weathermap.data.remote.dto.RealtimeWeatherDto
import com.saxipapsi.weathermap.domain.repository.LocationRepository
import com.saxipapsi.weathermap.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class SelectedLocationUseCase(private val locationRepository: LocationRepository, private val repo: WeatherRepository) {

     fun getAllSelected(): Flow<Resource<List<SelectedLocationModel>>> = flow {
        try {
            emit(Resource.Loading())
            var selectedLocations = locationRepository.loadAll().map { it.toSelectedLocationModel() }
            selectedLocations.forEach {
                it.apply {
                    realtimeWeather = repo.getRealtimeWeather("${it.latitude},${it.longitude}")
                    //forecastWeather = repo.getForecastWeather("${it.latitude},${it.longitude}", 3)
                }
            }
            selectedLocations = selectedLocations.distinctBy { it.name }.sortedBy { it.id }
            emit(Resource.Success(selectedLocations))
        } catch (err: HttpException) {
            emit(Resource.Error(err.localizedMessage ?: "An unexpected error occured."))
        } catch (err: IOException) {
            emit(Resource.Error("Couldn't reach server. Please check your internet connection"))
        }
    }

    fun getCurrentSelected(): Flow<Resource<SelectedLocationModel?>> = flow {
        try {
            emit(Resource.Loading())
            val selectedLocation = locationRepository.getCurrentSelected()?.toSelectedLocationModel()?.apply {
                realtimeWeather = repo.getRealtimeWeather("${latitude},${longitude}")
            }
            emit(Resource.Success(selectedLocation))
        } catch (err: HttpException) {
            emit(Resource.Error(err.localizedMessage ?: "An unexpected error occured."))
        } catch (err: IOException) {
            emit(Resource.Error("Couldn't reach server. Please check your internet connection"))
        }
    }

}

data class SelectedLocationModel(
    val id: Long,
    val latitude: String? = null,
    val longitude: String? = null,
    val name: String? = null,
    val region: String? = null,
    val country: String? = null,
    val isCurrentSelected: Boolean = false,
    var realtimeWeather: RealtimeWeatherDto? = null,
    var forecastWeather: ForecastWeatherDto? = null
)