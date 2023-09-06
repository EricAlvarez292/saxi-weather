package com.saxipapsi.weathermap.data.repository

import com.saxipapsi.weathermap.data.db.dao.LocationDao
import com.saxipapsi.weathermap.data.db.entity.LocationEntity
import com.saxipapsi.weathermap.data.remote.WeatherApi
import com.saxipapsi.weathermap.data.remote.dto.LocationSearchDto
import com.saxipapsi.weathermap.domain.repository.LocationRepository

class LocationRepositoryImpl(private val locationDao: LocationDao, private val weatherApi: WeatherApi) : LocationRepository {
    override suspend fun loadAll(): List<LocationEntity> = locationDao.loadAll()
    override suspend fun insert(entity: LocationEntity): Long {
        onManageDeSelectCurrentSelected()
        return locationDao.insert(entity)
    }

    override suspend fun getCurrentSelected(): LocationEntity? = locationDao.getCurrentSelected()
    override suspend fun getById(id: Long): LocationEntity? = locationDao.getById(id)
    override suspend fun onManageSelected(id: Long) {
        onManageDeSelectCurrentSelected()
        locationDao.onManageSelected(id)
    }
    override suspend fun onManageDeSelected(id: Long) {
        locationDao.onManageDeSelected(id)
        getById(id)?.let { location -> if (location.isCurrentSelected) loadAll().filter { it.id != id }.takeIf { list -> list.isNotEmpty() }?.first()?.let { defaultSelected -> onManageSelected(defaultSelected.id!!) } }
    }
    override suspend fun onManageDeSelectCurrentSelected() = locationDao.onManageDeSelectCurrentSelected()
    override suspend fun clear(): Int = locationDao.clear()
    override suspend fun getSearchWeather(data: String): List<LocationSearchDto> = weatherApi.getSearchWeather(data)
}