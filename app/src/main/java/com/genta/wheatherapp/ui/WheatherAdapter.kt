package com.genta.wheatherapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.genta.wheatherapp.BuildConfig
import com.genta.wheatherapp.data.ListItem
import com.genta.wheatherapp.databinding.RowItemWeatherBinding
import com.genta.wheatherapp.utils.HelperFunction.formatterDegree
import com.genta.wheatherapp.utils.iconSizeWeather2x
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WheatherAdapter : RecyclerView.Adapter<WheatherAdapter.MyViewHolder>(){

    private var listWeather = ArrayList<ListItem>()
    class MyViewHolder(val binding: RowItemWeatherBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        RowItemWeatherBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    )


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = listWeather[position]
        holder.binding.apply {
            val maxDegree = "Max:" + formatterDegree(data.main?.tempMax)
            tvMaxDegree.text = maxDegree

            val minDegree = "Man:" + formatterDegree(data.main?.tempMin)
            tvMinDegree.text = minDegree

            val date = data.dtTxt?.take(10)
            val time = data.dtTxt?.takeLast(8)
            val dateArray = date?.split("-")?.toTypedArray()
            val timeArray = time?.split(":")?.toTypedArray()

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, Integer.parseInt(dateArray?.get(0)as String))
            calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1])- 1)
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]))

            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray?.get(0) as String))
            calendar.set(Calendar.MINUTE,0)

            val dataFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
                .format(calendar.time).toString()
            val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                .format(calendar.time).toString()

            tvItemDate.text = dataFormat
            tvItemTime.text = timeFormat

            val icon = data.weather?.get(0)?.icon
            val iconUrl= BuildConfig.ICON_URL + icon + iconSizeWeather2x
            Glide.with(imgItemWeather.context).load(iconUrl).into(imgItemWeather)

        }
    }

    override fun getItemCount() = listWeather.size

    fun setData(data: List<ListItem>?){
        if(data == null) return
        listWeather.clear()
        listWeather.addAll(data)

    }
}