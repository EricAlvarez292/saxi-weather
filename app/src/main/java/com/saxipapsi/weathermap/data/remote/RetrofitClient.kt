package com.saxipapsi.weathermap.data.remote

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.google.gson.GsonBuilder
import com.saxipapsi.weathermap.common.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun networkModule(app: Application) = module {
    single { WeatherAuthInterceptor(app) }
    single { getOkHttpClient(get()) }
    single { getRetrofit(get()) }
    single { getWeatherApi(get()) }
    single { RetrofitClient(get()).initialize() }
}

class WeatherAuthInterceptor(private val application: Application) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
        val interceptedRequest = request.addHeader("Content-Type", "application/json")
            .addHeader("X-RapidAPI-Host", Constant.HOST)
            .addHeader("X-RapidAPI-Key", getAuth())
            .method(original.method, original.body)
            .build()
        return chain.proceed(interceptedRequest)
    }

    private fun getAuth() : String = try {
        val ai: ApplicationInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { application.packageManager.getApplicationInfo(application.packageName, PackageManager.ApplicationInfoFlags.of(0))
        } else application.packageManager.getApplicationInfo(application.packageName, PackageManager.GET_META_DATA)
        val bundle = ai.metaData
        bundle.getString("WEATHER_API_TOKEN") ?: ""
    } catch (e: Exception) {
        Log.e("eric", "Dear developer. Don't forget to configure <meta-data android:name=\"my_test_metagadata\" android:value=\"testValue\"/> in your AndroidManifest.xml file.")
        ""
    }
}

private fun getOkHttpClient(weatherAuthInterceptor : WeatherAuthInterceptor): OkHttpClient = OkHttpClient()
    .newBuilder()
    .addInterceptor(weatherAuthInterceptor)
    .connectTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .build()

private fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
    return Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

private fun getWeatherApi(retrofit: Retrofit): WeatherApi = retrofit.create(WeatherApi::class.java)

class RetrofitClient(private val retrofit: Retrofit) {
    private lateinit var weatherApi: WeatherApi
    fun initialize() {
        weatherApi = retrofit.create(WeatherApi::class.java)
    }
}