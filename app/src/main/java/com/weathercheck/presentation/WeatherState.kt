package com.weathercheck.presentation

import com.weathercheck.data.model.MyWeather

data class WeatherState(
    val weatherData: MyWeather? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
