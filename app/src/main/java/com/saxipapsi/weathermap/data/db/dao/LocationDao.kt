package com.saxipapsi.weathermap.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.saxipapsi.weathermap.data.db.entity.LocationEntity

@Dao
interface LocationDao {

    @Query("SELECT * FROM table_location")
    suspend fun loadAll(): List<LocationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: LocationEntity): Long

    @Query("SELECT * FROM table_location WHERE is_current_selected = true")
    suspend fun getCurrentSelected(): LocationEntity?

    @Query("SELECT * FROM table_location WHERE id =:id")
    suspend fun getById(id: Long): LocationEntity?

    @Query("UPDATE table_location SET is_current_selected = true WHERE id = :id")
    suspend fun onManageSelected(id: Long)

    @Query("DELETE FROM table_location WHERE id = :id")
    suspend fun onManageDeSelected(id: Long)

    @Query("UPDATE table_location SET is_current_selected = false WHERE is_current_selected = true")
    suspend fun onManageDeSelectCurrentSelected()

    @Query("DELETE FROM table_location")
    suspend fun clear(): Int

}