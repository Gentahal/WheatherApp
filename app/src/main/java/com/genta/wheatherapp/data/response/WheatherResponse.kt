package com.genta.wheatherapp.data

import com.google.gson.annotations.SerializedName

data class WheatherResponse(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("weather")
    val whether: List<WeatherItem?>? = null,

    @field:SerializedName("main")
    val main: Main? = null
)

data class WeatherItem(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("main")
    val main: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("icon")
    val icon: String? = null
)
