package com.saxipapsi.weathermap.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val CITY_DB_TABLE_NAME = "table_city"

@Entity(tableName = CITY_DB_TABLE_NAME)
data class CityEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "latitude") val latitude: String? = null,
    @ColumnInfo(name = "longitude") val longitude: String? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "is_selected") val isSelected: Boolean = false,
    @ColumnInfo(name = "is_current_selected") val isCurrentSelected: Boolean = false,
    @ColumnInfo(name = "selected_at") val selectedAt: Long,
    /*Country State fk id*/
    @ColumnInfo(name = "state_id") var state_id: Int
)
