package com.saxipapsi.weathermap.domain.repository

import com.saxipapsi.weathermap.data.db.entity.CityEntity
import com.saxipapsi.weathermap.data.db.entity.CountryStateEntity

interface CountryStateRepository {
    suspend fun loadDataFromJson(): List<CountryStateEntity>
    suspend fun getCountryStatesBySearch(search: String): List<CountryStateEntity>
    suspend fun getCountryStateCitiesBySearch(search: String): List<CityEntity>

    suspend fun onSearchSelectStateById(id: Int)
    suspend fun onSearchSelectCityById(id: Int)

    suspend fun onManageSelectStateById(id: Int)
    suspend fun onManageSelectCityById(id: Int)
    suspend fun onManageDeSelectStateById(id: Int)
    suspend fun onManageDeSelectCityById(id: Int)


    suspend fun getSelectedStates(): List<CountryStateEntity>
    suspend fun getSelectedCities(): List<CityEntity>
    suspend fun getSelectedCityByCoordinates(latitude: String, longitude: String): CityEntity?
    suspend fun getSelectedStateByCoordinates(latitude: String, longitude: String): CountryStateEntity?
    suspend fun getAllCities(): List<CityEntity>
    suspend fun getAllStates(): List<CountryStateEntity>
}