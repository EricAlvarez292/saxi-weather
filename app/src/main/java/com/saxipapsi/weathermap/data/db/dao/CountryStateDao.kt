package com.saxipapsi.weathermap.data.db.dao

import androidx.room.*
import com.saxipapsi.weathermap.data.db.entity.CityEntity
import com.saxipapsi.weathermap.data.db.entity.CountryStateEntity
import com.saxipapsi.weathermap.data.db.entity.CountryStateWithCitiesEntity

@Dao
interface CountryStateDao {

    @Query("SELECT * FROM table_country_state")
    suspend fun loadAll(): List<CountryStateEntity>

    @Query("SELECT * FROM table_country_state")
    suspend fun loadAllWithCities(): List<CountryStateWithCitiesEntity>

    @Query("SELECT * FROM table_country_state WHERE id=:id ")
    fun getStateWithCitiesById(id: Int): CountryStateWithCitiesEntity?

    @Query("SELECT * FROM table_country_state WHERE LOWER(name) GLOB '*' || :search|| '*' ORDER BY name")
    suspend fun getStateBySearch(search: String): List<CountryStateEntity>

    @Query("UPDATE table_country_state SET is_selected = :isSelected, selected_at = :selectedAt WHERE id = :id")
    suspend fun onSearchSelected(id: Int, isSelected: Boolean, selectedAt: Long = System.currentTimeMillis())

    @Query("UPDATE table_country_state SET is_selected = true, is_current_selected = true WHERE id = :id")
    suspend fun onManageSelected(id: Int)

    @Query("UPDATE table_country_state SET is_selected = false, is_current_selected = false, selected_at = 0 WHERE id = :id")
    suspend fun onManageDeSelected(id: Int)

    @Query("UPDATE table_country_state SET is_current_selected = false WHERE is_current_selected = true")
    suspend fun onManageDeSelectCurrentSelected()

    @Query("UPDATE table_country_state SET is_selected = :isSelected WHERE latitude = :latitude AND longitude = :longitude")
    suspend fun onSelectDeselectByCoordinates(latitude: String, longitude: String, isSelected: Boolean)

    @Query("SELECT * FROM table_country_state WHERE latitude = :latitude AND longitude = :longitude")
    suspend fun getSelectedStateByCoordinates(latitude: String, longitude: String): CountryStateEntity?

    @Query("SELECT * FROM table_country_state WHERE is_selected = true")
    suspend fun getSelectedStates(): List<CountryStateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CountryStateEntity): Long

    @Query("DELETE FROM table_country_state")
    suspend fun clear(): Int

    @Query("SELECT count(1) where not exists (SELECT * from table_country_state)")
    suspend fun getOneAsEmpty(): Int

    suspend fun isEmpty() = getOneAsEmpty() == 1

    @Transaction
    suspend fun deleteAndInsert(entity: CountryStateEntity): Long {
        clear()
        return insert(entity)
    }


}