package com.saxipapsi.weathermap.domain.repository

import com.saxipapsi.weathermap.data.db.entity.LocationEntity
import com.saxipapsi.weathermap.data.remote.dto.LocationSearchDto

interface LocationRepository {
    suspend fun loadAll(): List<LocationEntity>
    suspend fun insert(entity: LocationEntity): Long
    suspend fun getCurrentSelected(): LocationEntity?
    suspend fun getById(id: Long): LocationEntity?
    suspend fun onManageSelected(id: Long)
    suspend fun onManageDeSelected(id: Long)
    suspend fun onManageDeSelectCurrentSelected()
    suspend fun clear(): Int
    suspend fun getSearchWeather(data: String): List<LocationSearchDto>
}