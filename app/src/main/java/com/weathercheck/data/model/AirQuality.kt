package com.weathercheck.data.model

import com.google.gson.annotations.SerializedName


data class AirQuality (

  @SerializedName("co"             ) var co             : String? = null,
  @SerializedName("no2"            ) var no2            : String? = null,
  @SerializedName("o3"             ) var o3             : String? = null,
  @SerializedName("so2"            ) var so2            : String? = null,
  @SerializedName("pm2_5"          ) var pm25           : String? = null,
  @SerializedName("pm10"           ) var pm10           : String? = null,
  //@SerializedName("us-epa-index"   ) var us-epa-index   : String? = null,
//  @SerializedName("gb-defra-index" ) var gb-defra-index : String? = null

)