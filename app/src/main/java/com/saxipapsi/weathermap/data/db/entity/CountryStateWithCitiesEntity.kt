package com.saxipapsi.weathermap.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CountryStateWithCitiesEntity(
    @Embedded val state: CountryStateEntity,
    @Relation(parentColumn = "id", entityColumn = "state_id")
    val cities: List<CityEntity>
)
