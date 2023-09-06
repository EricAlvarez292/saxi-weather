package com.saxipapsi.weathermap.data.remote.dto

import com.saxipapsi.weathermap.domain.use_case.geo.SearchLocationModel

data class LocationSearchDto(
    val id: Int,
    val lat: String? = null,
    val lon: String? = null,
    val name: String? = null,
    val region: String? = null,
    val country: String? = null,
    val url: String? = null,
) {
    fun toSearchModel() = SearchLocationModel(
        latitude = lat,
        longitude = lon,
        name = name,
        region = region,
        country = country,
    )
}


