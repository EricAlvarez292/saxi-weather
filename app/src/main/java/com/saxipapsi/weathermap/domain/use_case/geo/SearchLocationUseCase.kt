package com.saxipapsi.weathermap.domain.use_case.geo

import android.util.Log
import com.saxipapsi.weathermap.common.Resource
import com.saxipapsi.weathermap.data.db.entity.LocationEntity
import com.saxipapsi.weathermap.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class SearchLocationUseCase(private val locationRepository: LocationRepository) {

    operator fun invoke(search: String): Flow<Resource<List<SearchLocationModel>>> = flow {
        try {
            emit(Resource.Loading())
            val locations = locationRepository.getSearchWeather(search)
            val finalData = locations.map { it.toSearchModel() }
            emit(Resource.Success(finalData))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Please check your internet connection"))
        }
    }
}

data class SearchLocationModel
    (
    val latitude: String? = null,
    val longitude: String? = null,
    val name: String? = null,
    val region: String? = null,
    val country: String? = null,
) {

    fun toLocationEntity(isCurrentSelected: Boolean = false) = LocationEntity(
        name = name,
        region = region,
        country = country,
        latitude = latitude,
        longitude = longitude,
        isCurrentSelected = isCurrentSelected
    )

}



