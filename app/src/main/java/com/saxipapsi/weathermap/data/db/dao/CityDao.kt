package com.saxipapsi.weathermap.data.db.dao

import androidx.room.*
import com.saxipapsi.weathermap.data.db.entity.CityEntity
import com.saxipapsi.weathermap.data.db.entity.CountryStateEntity

@Dao
interface CityDao {

    @Query("SELECT * FROM table_city")
    suspend fun loadAll(): List<CityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CityEntity): Long

    @Query("SELECT * FROM table_city WHERE LOWER(name) GLOB '*' || :search|| '*' ORDER BY name")
    suspend fun getCityBySearch(search: String): List<CityEntity>

    @Query("UPDATE table_city SET is_selected = :isSelected, selected_at = :selectedAt WHERE id = :id")
    suspend fun onSearchSelected(id: Int, isSelected: Boolean, selectedAt: Long = System.currentTimeMillis())

    @Query("UPDATE table_city SET is_selected = true, is_current_selected = true WHERE id = :id")
    suspend fun onManageSelected(id: Int)

    @Query("UPDATE table_city SET is_selected = false, is_current_selected = false, selected_at = 0 WHERE id = :id")
    suspend fun onManageDeSelected(id: Int)

    @Query("UPDATE table_city SET is_current_selected = false WHERE is_current_selected = true")
    suspend fun onManageDeSelectCurrentSelected()

    @Query("SELECT * FROM table_city WHERE is_selected = true")
    suspend fun getSelectedCities(): List<CityEntity>

    @Query("UPDATE table_city SET is_selected = :isSelected WHERE latitude = :latitude AND longitude = :longitude")
    suspend fun onSelectDeselectByCoordinates(latitude: String, longitude: String, isSelected: Boolean)

    @Query("SELECT * FROM table_city WHERE latitude = :latitude AND longitude = :longitude")
    suspend fun getSelectedCityByCoordinates(latitude: String, longitude: String): CityEntity?

    @Query("DELETE FROM table_city")
    suspend fun clear(): Int

    @Transaction
    suspend fun deleteAndInsert(entity: CityEntity): Long {
        clear()
        return insert(entity)
    }
}