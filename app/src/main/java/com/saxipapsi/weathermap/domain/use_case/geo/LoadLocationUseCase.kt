package com.saxipapsi.weathermap.domain.use_case.geo

import android.util.Log
import com.saxipapsi.weathermap.common.Resource
import com.saxipapsi.weathermap.data.db.entity.CountryStateEntity
import com.saxipapsi.weathermap.domain.repository.CountryStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class LoadLocationUseCase(private val repo: CountryStateRepository) {
    operator fun invoke(): Flow<Resource<List<CountryStateEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val data = repo.loadDataFromJson()
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Please check your internet connection"))
        }
    }
}