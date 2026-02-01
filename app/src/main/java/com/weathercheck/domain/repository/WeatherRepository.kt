package com.weathercheck.domain.repository

import com.weathercheck.data.model.MyWeather

interface WeatherRepository {
    suspend fun getForecast(query: String, days: Int): Result<MyWeather>
}
