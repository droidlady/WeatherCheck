package com.weathercheck.data.model

import com.google.gson.annotations.SerializedName

data class MyWeather(

    @SerializedName("request"  ) var request  : Request?  = Request(),
    @SerializedName("location" ) var location : Location? = Location(),
    @SerializedName("current"  ) var current  : Current?  = Current()

)
