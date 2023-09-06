package com.saxipapsi.weathermap.presentation.forecast_weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saxipapsi.weathermap.common.Resource
import com.saxipapsi.weathermap.data.remote.dto.ForecastWeatherDto
import com.saxipapsi.weathermap.domain.use_case.weather.GetForecastWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ForecastWeatherViewModel(private val getForecastWeatherUseCase: GetForecastWeatherUseCase) : ViewModel() {

    private val _state = MutableStateFlow(ForecastWeatherState())
    val state: StateFlow<ForecastWeatherState> = _state

    fun getWeather(id: String) {
        getForecastWeatherUseCase(data = id).onEach { resource ->
            when (resource) {
                is Resource.Loading -> _state.value = ForecastWeatherState(isLoading = true)
                is Resource.Error -> _state.value = ForecastWeatherState(error = resource.message ?: "Unexpected error occured.")
                is Resource.Success -> _state.value = ForecastWeatherState(data = resource.data)
            }
        }.launchIn(viewModelScope)
    }
}

data class ForecastWeatherState(
    val isLoading: Boolean = false,
    val data: ForecastWeatherDto? = null,
    val error: String = ""
)