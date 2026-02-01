package com.weathercheck.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weathercheck.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.weathercheck.util.Constants
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _state = mutableStateOf(WeatherState())
    val state: State<WeatherState> = _state

    init {
        loadWeatherInfo()
    }

    fun loadWeatherInfo() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )
            repository.getForecast(Constants.CITY_LAT, Constants.DAYS.toIntOrNull() ?: 10)
                .onSuccess { weatherData ->
                    _state.value = _state.value.copy(
                        weatherData = weatherData,
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _state.value = _state.value.copy(
                        weatherData = null,
                        isLoading = false,
                        error = exception.message ?: "An unknown error occurred"
                    )
                }
        }
    }
}
