package com.saxipapsi.weathermap.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.lifecycleScope
import com.saxipapsi.weathermap.R
import com.saxipapsi.weathermap.data.remote.dto.HourDto
import com.saxipapsi.weathermap.data.remote.dto.RealtimeWeatherDto
import com.saxipapsi.weathermap.databinding.ActivityMainWeatherBinding
import com.saxipapsi.weathermap.presentation.forecast_weather.ForecastWeatherViewModel
import com.saxipapsi.weathermap.presentation.forecast_weather.components.ForecastHoursAdapter
import com.saxipapsi.weathermap.presentation.geo.LocationViewModel
import com.saxipapsi.weathermap.presentation.realtime_weather.components.RealTimeWeatherAdapter
import com.saxipapsi.weathermap.utility.extension.load
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf
import java.util.*
import kotlin.concurrent.schedule

class MainActivity() : AppCompatActivity() {

    private fun initAddOnOffsetChangedListener(){
        binding.appbarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = appBarLayout?.totalScrollRange ?: -1
            }
            _scrollState.value = scrollRange + verticalOffset == 0
        }
    }

    private suspend fun onScrollViewUpdateListener(){
        scrollState.collectLatest{ isShow ->
            if (isShow) {
                hideOption(R.id.action_settings)
                hideOption(R.id.action_add)
                val header = "${binding.tvLocation.text}, ${binding.tvRegion.text}"
                binding.toolbarTitle.text = header
            } else {
                showOption(R.id.action_settings)
                showOption(R.id.action_add)
                binding.toolbarTitle.text = ""
            }
        }
    }

    private fun hideOption(id: Int) {
        val item: MenuItem? = menu?.findItem(id)
        item?.isVisible = false
    }

    private fun showOption(id: Int) {
        val item: MenuItem? = menu?.findItem(id)
        item?.isVisible = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        this.menu = menu!!
//        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    private lateinit var binding: ActivityMainWeatherBinding

    private val savedStateHandle by lazy { SavedStateHandle() }
    private val forecastWeatherViewModel by stateViewModel<ForecastWeatherViewModel> { parametersOf(savedStateHandle) }

    private val locationViewModel: LocationViewModel by inject()
    private val forecastAdapter: RealTimeWeatherAdapter by lazy { RealTimeWeatherAdapter() }
    private val forecastHourAdapter : ForecastHoursAdapter by lazy { ForecastHoursAdapter() }

    private val _scrollState = MutableStateFlow(false)
    private val scrollState: StateFlow<Boolean> = _scrollState

    private var menu: Menu? = null
    private var scrollRange = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.forecastLayout.rvForecast.adapter = forecastAdapter
        binding.forecastHourLayout.rvForecast.adapter = forecastHourAdapter
        binding.fab.setOnClickListener { ManageLocationActivity.start(this) }
        initAddOnOffsetChangedListener()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        locationViewModel.currentSelectedLocationState()
    }

    private fun initObservers() {
        lifecycleScope.launch { onScrollViewUpdateListener() }
        lifecycleScope.launch { onSelectedLocationStateObserver() }
        lifecycleScope.launch { observeForecastWeather() }
    }

    private suspend fun onSelectedLocationStateObserver(){
        locationViewModel.currentSelectedLocationState.collectLatest { result ->
            binding.realtimeLoading.loading.visibility = if (result.isLoading) VISIBLE else GONE
            binding.realtimeLoading.tvError.visibility = if (result.error.isNotEmpty()) VISIBLE else GONE
            binding.realtimeLoading.tvError.text = result.error
            result.data?.let { data ->
                observeRealTimeWeather(data.realtimeWeather!!)
                forecastWeatherViewModel.getWeather("${data.latitude},${data.longitude}")
            }
        }
    }

    private fun observeRealTimeWeather(data: RealtimeWeatherDto) {
        binding.tvLocation.text = data.location.name
        binding.tvRegion.text = data.location.region
        binding.tvCountry.text = data.location.country
        binding.tvDegreeCelsius.text = data.current.temp_c.toInt().toString()
        val realFeel = "RealFeel: ${data.current.feelslike_c}"
        binding.tvRealFeel.text = realFeel
        binding.tvCondition.text = data.current.condition.text
        binding.ivIcon.load("https:${data.current.condition.icon}")
        binding.gHeader.visibility = VISIBLE
    }

    private suspend fun observeForecastWeather() {
        forecastWeatherViewModel.state.collect { result ->
            binding.forecastLayout.forecastLoading.loading.visibility = if (result.isLoading) VISIBLE else GONE
            binding.forecastLayout.forecastLoading.tvError.visibility = if (result.error.isNotEmpty()) VISIBLE else GONE
            binding.forecastLayout.forecastLoading.tvError.text = result.error
            result.data?.let { data ->
                Log.d("weather", "Forecast Weather Data : ${data.forecast.forecastday.size}")
                forecastAdapter.differ.submitList(data.forecast.forecastday)
                val forecastHour : MutableList<HourDto> = data.forecast.forecastday[0].hour as MutableList<HourDto>
                val first = forecastHour.first()
                forecastHour.removeFirst()
                forecastHour.add(first)
                observeForecastHourWeather(forecastHour)
            }
        }
    }

    private fun observeForecastHourWeather(hour: List<HourDto>) {
        binding.forecastHourLayout.forecastHourLoading.loading.visibility = VISIBLE
        binding.forecastHourLayout.forecastHourLoading.tvError.visibility = GONE
        Timer().schedule(10) {
            runOnUiThread {
                binding.forecastHourLayout.forecastHourLoading.loading.visibility = GONE
                forecastHourAdapter.differ.submitList(hour)
            }
        }
    }

    companion object{
        fun start(context : Context){ context.startActivity(Intent(context, MainActivity::class.java))}
    }

}