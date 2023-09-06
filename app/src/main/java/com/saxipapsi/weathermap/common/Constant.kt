package com.saxipapsi.weathermap.common

object Constant {
    const val FORECAST_ID = "fcWeatherId"
    const val REALTIME_ID = "rtWeatherId"
    const val DEFAULT_LATITUDE = "40.71"
    const val DEFAULT_LONGITUDE = "-74.01"
    const val DEFAULT_ID = "40.71,-74.01"

    const val HOST = "weatherapi-com.p.rapidapi.com"
    const val BASE_URL = "https://$HOST"
}

object GeoLocationType {
    const val STATE = "STATE"
    const val CITY = "CITY"
    const val DEFAULT = "LOCATION"
}
