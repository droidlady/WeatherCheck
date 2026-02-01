package com.weathercheck.data.remote

import com.weathercheck.data.model.MyWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("current")
    suspend fun getForecast(
        @Query("access_key") apiKey: String,
        @Query("query") query: String,
        @Query("forecast_days") days: Int
    ): MyWeather

    companion object {
        const val BASE_URL = "https://api.weatherstack.com/"
    }
}
