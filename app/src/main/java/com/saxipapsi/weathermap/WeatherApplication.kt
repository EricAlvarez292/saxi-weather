package com.saxipapsi.weathermap

import android.app.Application
import com.saxipapsi.weathermap.di.AppModule.initKoin

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(this)
    }
}