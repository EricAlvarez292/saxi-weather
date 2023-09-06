package com.saxipapsi.weathermap.domain.use_case.geo

import com.saxipapsi.weathermap.common.Resource
import com.saxipapsi.weathermap.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class ManageSelectedLocationUseCase(private val locationRepository: LocationRepository) {

    fun onSearchSelected(searchLocationModel : SearchLocationModel): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val data = searchLocationModel.toLocationEntity(true)
            locationRepository.insert(data)
            emit(Resource.Success(true))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Please check your internet connection"))
        }
    }

    fun onManageSelectedById(id: Long, isSelected : Boolean = false): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            if (isSelected) locationRepository.onManageSelected(id) else locationRepository.onManageDeSelected(id)
            emit(Resource.Success(true))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Please check your internet connection"))
        }
    }
}