package com.saxipapsi.weathermap.presentation.forecast_weather.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.saxipapsi.weathermap.R
import com.saxipapsi.weathermap.data.remote.dto.HourDto
import com.saxipapsi.weathermap.utility.extension.load
import com.saxipapsi.weathermap.utility.extension.toDateFormat

class ForecastHoursAdapter : RecyclerView.Adapter<ForecastHoursViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHoursViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_forecast_hours_item, parent, false)
        return ForecastHoursViewHolder(view)
    }
    override fun onBindViewHolder(holder: ForecastHoursViewHolder, position: Int) { holder.bind(differ.currentList[position]) }
    override fun getItemCount(): Int = differ.currentList.size
    private val diffUtil : DiffUtil.ItemCallback<HourDto> = object : DiffUtil.ItemCallback<HourDto>(){
        override fun areItemsTheSame(oldItem: HourDto, newItem: HourDto): Boolean  = oldItem == newItem
        override fun areContentsTheSame(oldItem: HourDto, newItem: HourDto): Boolean = oldItem == newItem
    }
    val differ = AsyncListDiffer(this, diffUtil)
}

class ForecastHoursViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvCelsius: TextView = itemView.findViewById(R.id.tvCelsius)
    private val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
    private val tvCondition: TextView = itemView.findViewById(R.id.tvCondition)
    private val tvTime: TextView = itemView.findViewById(R.id.tvTime)

    fun bind(hourDto : HourDto){
        tvCelsius.text = hourDto.temp_c.toInt().toString()
        ivIcon.load("https:${hourDto.condition.icon}")
        tvCondition.text = hourDto.condition.text
        tvTime.text = hourDto.time.toDateFormat("HH:mm", "yyyy-MM-dd hh:mm")
    }
}