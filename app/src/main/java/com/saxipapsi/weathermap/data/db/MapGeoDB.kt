package com.saxipapsi.weathermap.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.saxipapsi.weathermap.data.db.dao.CityDao
import com.saxipapsi.weathermap.data.db.dao.CountryStateDao
import com.saxipapsi.weathermap.data.db.dao.LocationDao
import com.saxipapsi.weathermap.data.db.entity.CityEntity
import com.saxipapsi.weathermap.data.db.entity.CountryStateEntity
import com.saxipapsi.weathermap.data.db.entity.LocationEntity

@Database(entities = [CountryStateEntity::class, CityEntity::class, LocationEntity::class], version = 1)
abstract class MapGeoDB : RoomDatabase() {

    abstract fun countryStateDao(): CountryStateDao
    abstract fun cityDao(): CityDao
    abstract fun locationDao() : LocationDao

    companion object {
        private const val MAP_GEO_DB_NAME = "map_geo_database"
        fun build(context: Context): MapGeoDB = Room.databaseBuilder(context, MapGeoDB::class.java, MAP_GEO_DB_NAME).build()
    }
}


