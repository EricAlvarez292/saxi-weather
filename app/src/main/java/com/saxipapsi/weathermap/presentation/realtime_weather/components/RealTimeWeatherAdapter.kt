package com.saxipapsi.weathermap.presentation.realtime_weather.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.saxipapsi.weathermap.R
import com.saxipapsi.weathermap.data.remote.dto.ForecastdayDto
import com.saxipapsi.weathermap.utility.extension.load
import com.saxipapsi.weathermap.utility.extension.toDateFormat

class RealTimeWeatherAdapter() : RecyclerView.Adapter<RealTimeWeatherItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealTimeWeatherItem {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_forecast_item, parent, false)
        return RealTimeWeatherItem(view)
    }
    override fun onBindViewHolder(holder: RealTimeWeatherItem, position: Int) { holder.bind(differ.currentList[position]) }
    override fun getItemCount(): Int  = differ.currentList.size

    private val diffUtil = object : DiffUtil.ItemCallback<ForecastdayDto>(){
        override fun areItemsTheSame(oldItem: ForecastdayDto, newItem: ForecastdayDto): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: ForecastdayDto, newItem: ForecastdayDto): Boolean = oldItem == newItem
    }
    val differ = AsyncListDiffer(this, diffUtil)
}

class RealTimeWeatherItem(itemView: View) : RecyclerView.ViewHolder(itemView){
    private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
    private val tvCondition: TextView = itemView.findViewById(R.id.tvCondition)
    private val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
    fun bind(weather : ForecastdayDto){
        tvDate.text = weather.date.toDateFormat()
        tvCondition.text = weather.day.condition.text
        ivIcon.load( "https:${weather.day.condition.icon}")
    }
}
