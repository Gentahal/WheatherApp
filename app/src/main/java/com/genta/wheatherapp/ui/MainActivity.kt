package com.genta.wheatherapp.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.genta.wheatherapp.BuildConfig
import com.genta.wheatherapp.R
import com.genta.wheatherapp.data.ForecastResponse
import com.genta.wheatherapp.data.WheatherResponse
import com.genta.wheatherapp.databinding.ActivityMainBinding
import com.genta.wheatherapp.utils.HelperFunction.formatterDegree
import com.genta.wheatherapp.utils.LOCATION_PERMISION_REQ_CODE
import com.genta.wheatherapp.utils.iconSizeWeather4x
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding as ActivityMainBinding

    private var _viewModel : MainViewModel? = null
    private val viewModel get() = _viewModel as MainViewModel

    private val weatherAdapter by lazy { WheatherAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)// biar statusBarnya transparant
        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView)
        windowInsetsController?.isAppearanceLightNavigationBars = true

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        searchCity()

        viewModel.getWeatherByCity().observe(this) {
            setupView(it,null)
        }

        viewModel.getForecastByCity().observe(this){
            setupView(null, it)
        }

        getWeatherCurrentLocation()
    }

    private fun getWeatherCurrentLocation() {
        val fusedLocationClient : FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                LOCATION_PERMISION_REQ_CODE
                )
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener{
            val lat = it.latitude
            val lon = it.longitude

            viewModel.weatherCurrentLocation(lat, lon)
            viewModel.forecastByCurrentLocation(lat, lon)
        }
            .addOnFailureListener {
                Log.e("MainActivity", "FusedLocationError: Failed Getting current Location")
            }

        viewModel.weatherCurrentLocation(1.9, 9.9)
        viewModel.getWeatherByCurrentLocation().observe(this){
            setupView(it, null)
        }
        viewModel.getForecastByCurrentLocation().observe(this){
            setupView(null,it)
        }
    }

    private fun setupView(weather: WheatherResponse?, forecast: ForecastResponse?){
        binding.apply {
            weather?.let {
                tvDegree.text = formatterDegree(it.main?.temp)
                tvCity.text = weather.name

                val icon = weather.whether?.get(0)?.icon
                val iconUrl = BuildConfig.ICON_URL + icon + iconSizeWeather4x
                Glide.with(applicationContext).load(iconUrl).into(imgIcWeather)

                setupBackgroundWeather(weather.whether?.get(0)?.id, icon)

            }
            rvForecastWheather.apply {
                weatherAdapter.setData(forecast?.list)
                layoutManager = LinearLayoutManager(
                    applicationContext,LinearLayoutManager.HORIZONTAL,false
                )
                adapter = weatherAdapter
            }
        }
    }

    private fun setupBackgroundWeather(idWeather: Int?, icon: String?) { //fun unutk ngambil drawable
        idWeather?.let {
            when (idWeather) {
                in resources.getIntArray(R.array.thunderstorm_id_list) ->
                    setImageBackground(R.drawable.thunderstorm)
                in resources.getIntArray(R.array.drizzle_id_list) ->
                    setImageBackground(R.drawable.drizzle)
                in resources.getIntArray(R.array.rain_id_list) ->
                    setImageBackground(R.drawable.rain)
                in resources.getIntArray(R.array.freezing_rain_id_list) ->
                    setImageBackground(R.drawable.freezing_rain)
                in resources.getIntArray(R.array.snow_id_list) ->
                    setImageBackground(R.drawable.snow)
                in resources.getIntArray(R.array.sleet_id_list) ->
                    setImageBackground(R.drawable.sleet)

                in resources.getIntArray(R.array.clear_id_list) -> {
                    when (icon)  {
                        "01d" -> setImageBackground(R.drawable.clear)
                        "01n" -> setImageBackground(R.drawable.clear)
                    }
                }

                in resources.getIntArray(R.array.clouds_id_list) ->
                    setImageBackground(R.drawable.lightcloud)
                in resources.getIntArray(R.array.heavy_clouds_id_list) ->
                    setImageBackground(R.drawable.heavycloud)
                in resources.getIntArray(R.array.fog_id_list) ->
                    setImageBackground(R.drawable.fog)
                in resources.getIntArray(R.array.sand_id_list) ->
                    setImageBackground(R.drawable.sand)
                in resources.getIntArray(R.array.dust_id_list) ->
                    setImageBackground(R.drawable.dust)
                in resources.getIntArray(R.array.volcanic_ash_id_list) ->
                    setImageBackground(R.drawable.volcanic)
                in resources.getIntArray(R.array.squalls_id_list) ->
                    setImageBackground(R.drawable.squalls)
                in resources.getIntArray(R.array.tornado_id_list) ->
                    setImageBackground(R.drawable.tornado)

            }
        }
    }

    private fun setImageBackground(image: Int) {
        Glide.with(this).load(image).into(binding.imgBgWeather)
    }

    private fun searchCity(){
        binding.edtSearch.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        viewModel.searchByCity(it)
                        viewModel.forecastByCity(it)
//                        Log.i("MainActivity", "onQueryTextSubmit: OK")
                    }
                    try {
                        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

                        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken,0)
                    } catch (e: Throwable) {
                        Log.e("MainActivity", "hideSoftWindow: $e")
                    }
                    return  true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            }
        )
    }
}