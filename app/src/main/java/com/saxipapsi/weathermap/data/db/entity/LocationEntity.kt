package com.saxipapsi.weathermap.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.saxipapsi.weathermap.domain.use_case.geo.SelectedLocationModel


const val LOCATION_DB_TABLE_NAME = "table_location"

@Entity(tableName = LOCATION_DB_TABLE_NAME)
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "latitude") val latitude: String? = null,
    @ColumnInfo(name = "longitude") val longitude: String? = null,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "region") val region: String? = null,
    @ColumnInfo(name = "country") val country: String? = null,
    @ColumnInfo(name = "is_current_selected") val isCurrentSelected: Boolean = false,
) {

    fun toSelectedLocationModel() = SelectedLocationModel(
        id = id!!,
        latitude = latitude,
        longitude = longitude,
        name = name,
        region = region,
        country = country,
        isCurrentSelected,
    )
}