package com.weathercheck.data.repository

import com.pratibha.myweathercheck.BuildConfig
import com.weathercheck.data.remote.WeatherApi
import com.weathercheck.domain.repository.WeatherRepository
import com.weathercheck.data.model.MyWeather

class WeatherRepositoryImpl(
    private val api: WeatherApi
) : WeatherRepository {
    override suspend fun getForecast(query: String, days: Int): Result<MyWeather> {
        return try {
            val response = api.getForecast(BuildConfig.access_key, query, days)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
