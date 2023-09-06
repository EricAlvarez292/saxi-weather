package com.saxipapsi.weathermap.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

const val COUNTRY_STATE_DB_TABLE_NAME = "table_country_state"

@Entity(tableName = COUNTRY_STATE_DB_TABLE_NAME)
data class CountryStateEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "country_id") val country_id: Int,
    @ColumnInfo(name = "latitude") val latitude: String? = null,
    @ColumnInfo(name = "longitude") val longitude: String? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "state_code") val state_code: String,
    @ColumnInfo(name = "is_selected") val isSelected: Boolean = false,
    @ColumnInfo(name = "is_current_selected") val isCurrentSelected: Boolean = false,
    @ColumnInfo(name = "selected_at") val selectedAt: Long,
) {
    @Ignore
    val cities: List<CityEntity> = emptyList()
}
