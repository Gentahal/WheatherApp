package com.genta.wheatherapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.genta.wheatherapp.data.ForecastResponse
import com.genta.wheatherapp.data.WeatherItem
import com.genta.wheatherapp.data.WheatherResponse
import com.genta.wheatherapp.data.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val weatherByCity = MutableLiveData<WheatherResponse>()
    private val forecastBycity = MutableLiveData<ForecastResponse>()

    private val weatherByCurrentLocation = MutableLiveData<WheatherResponse>()
    private val forecastByCurrentLocation = MutableLiveData<ForecastResponse>()

    fun searchByCity(city: String) {
        ApiClient().getApiSercice().weatherByCity(city).enqueue(object : Callback<WheatherResponse> {
            override fun onResponse(
                call: Call<WheatherResponse>,
                response: Response<WheatherResponse>
            ) {
                if (response.isSuccessful) weatherByCity.postValue(response.body())
            }

            override fun onFailure(call: Call<WheatherResponse>, t: Throwable) {
                Log.e("FailureCallApi", t.message.toString())
            }

        })
    }

    fun getWeatherByCity() : LiveData<WheatherResponse> = weatherByCity

    fun forecastByCity(city: String) {
        ApiClient().getApiSercice().forecastByCity(city).enqueue(object : Callback<ForecastResponse> {
            override fun onResponse(
                call: Call<ForecastResponse>,
                response: Response<ForecastResponse>
            ) {
                if (response.isSuccessful) forecastBycity.postValue(response.body())
            }

            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {

            }
        })
    }
    fun getForecastByCity() : LiveData<ForecastResponse> = forecastBycity

    fun weatherCurrentLocation(lat: Double, lon: Double){
        ApiClient().getApiSercice().weatherByLoc(lat, lon).enqueue(object : Callback<WheatherResponse> {
            override fun onResponse(
                call: Call<WheatherResponse>,
                response: Response<WheatherResponse>
            ) {
                weatherByCurrentLocation.postValue(response.body())
            }

            override fun onFailure(call: Call<WheatherResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getWeatherByCurrentLocation() : LiveData<WheatherResponse> = weatherByCurrentLocation

    fun forecastByCurrentLocation(lat: Double, lon: Double){
        ApiClient().getApiSercice().forecastByLoc(lat, lon).enqueue(object : Callback<ForecastResponse> {
            override fun onResponse(
                call: Call<ForecastResponse>,
                response: Response<ForecastResponse>
            ) {
                forecastByCurrentLocation.postValue(response.body())
            }

            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
    fun getForecastByCurrentLocation() : LiveData<ForecastResponse> = forecastByCurrentLocation
}