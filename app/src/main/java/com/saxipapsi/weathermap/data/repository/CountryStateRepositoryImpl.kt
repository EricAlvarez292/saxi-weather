package com.saxipapsi.weathermap.data.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saxipapsi.weathermap.R
import com.saxipapsi.weathermap.data.db.dao.CityDao
import com.saxipapsi.weathermap.data.db.dao.CountryStateDao
import com.saxipapsi.weathermap.data.db.entity.CityEntity
import com.saxipapsi.weathermap.data.db.entity.CountryStateEntity
import com.saxipapsi.weathermap.domain.repository.CountryStateRepository
import com.saxipapsi.weathermap.utility.file.RawFileLoader
import java.lang.reflect.Type


class CountryStateRepositoryImpl(private val rawFileLoader: RawFileLoader, private val countryStateDao: CountryStateDao, private val cityDao: CityDao) : CountryStateRepository {

    override suspend fun getCountryStatesBySearch(search: String): List<CountryStateEntity> = countryStateDao.getStateBySearch(search)
    override suspend fun getCountryStateCitiesBySearch(search: String): List<CityEntity> = cityDao.getCityBySearch(search)
    override suspend fun onSearchSelectStateById(id : Int) { countryStateDao.onSearchSelected(id, true)}
    override suspend fun onSearchSelectCityById(id : Int) {cityDao.onSearchSelected(id, true)}

    override suspend fun onManageSelectStateById(id : Int) {
        countryStateDao.onManageDeSelectCurrentSelected()
        countryStateDao.onManageSelected(id)
    }
    override suspend fun onManageSelectCityById(id : Int) {
        cityDao.onManageDeSelectCurrentSelected()
        cityDao.onManageSelected(id)
    }
    override suspend fun onManageDeSelectStateById(id: Int) {countryStateDao.onManageDeSelected(id)}
    override suspend fun onManageDeSelectCityById(id: Int) {cityDao.onManageDeSelected(id)}

    override suspend fun getSelectedStates(): List<CountryStateEntity> = countryStateDao.getSelectedStates()
    override suspend fun getSelectedCities(): List<CityEntity> = cityDao.getSelectedCities()
    override suspend fun getSelectedCityByCoordinates(latitude: String, longitude: String): CityEntity? = cityDao.getSelectedCityByCoordinates(latitude, longitude)
    override suspend fun getSelectedStateByCoordinates(latitude: String, longitude: String): CountryStateEntity? = countryStateDao.getSelectedStateByCoordinates(latitude, longitude)
    override suspend fun getAllCities(): List<CityEntity> = cityDao.getSelectedCities()
    override suspend fun getAllStates(): List<CountryStateEntity> = countryStateDao.getSelectedStates()
    override suspend fun loadDataFromJson(): List<CountryStateEntity> {
        val data = if (countryStateDao.isEmpty()) {
            try {
                val data = rawFileLoader.getRawString(R.raw.states_and_cities)
                val listType: Type = object : TypeToken<List<CountryStateEntity>>() {}.type
                val geoLocationData: List<CountryStateEntity> = Gson().fromJson(data, listType)
                geoLocationData.forEach { state ->
                    Log.d("eric", "A->")
                    countryStateDao.insert(state)
                    state.cities.forEach { city ->
                        Log.d("eric", "B->")
                        city.state_id = state.id
                        cityDao.insert(city)
                    }
                }
            } catch (e: Exception) {
                Log.d("eric", "C-> : ${e.localizedMessage}")
            }
            countryStateDao.loadAll()
        } else {
            countryStateDao.loadAll()
        }
        return data
    }

}