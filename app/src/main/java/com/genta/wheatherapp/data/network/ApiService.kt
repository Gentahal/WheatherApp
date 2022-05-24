package com.genta.wheatherapp.data.network


import com.genta.wheatherapp.BuildConfig
import com.genta.wheatherapp.data.ForecastResponse
import com.genta.wheatherapp.data.WheatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather")
    fun weatherByCity(
        @Query("q") city:String,
        @Query("appid") apiKey: String? = BuildConfig.API_KEY
    ) : Call<WheatherResponse>

    @GET("forecast")
    fun forecastByCity(
        @Query("q") city:String,
        @Query("appid") apiKey: String? = BuildConfig.API_KEY
    ): Call<ForecastResponse>

    @GET("weather")
    fun weatherByLoc(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String? = BuildConfig.API_KEY
    ): Call<WheatherResponse>

    @GET("forecast")
    fun forecastByLoc(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String? = BuildConfig.API_KEY
    ): Call<ForecastResponse>
}