package com.saxipapsi.weathermap.presentation.geo.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.saxipapsi.weathermap.R
import com.saxipapsi.weathermap.domain.use_case.geo.SearchLocationModel
import com.saxipapsi.weathermap.domain.use_case.geo.SelectedLocationModel

class SelectedLocationAdapter(private val onLocationManageListener : OnLocationManageListener? = null) : RecyclerView.Adapter<SelectedLocationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedLocationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_recent_search_location, parent, false)
        return SelectedLocationViewHolder(view, onLocationManageListener)
    }
    override fun onBindViewHolder(holder: SelectedLocationViewHolder, position: Int) { holder.bind(differ.currentList[position])}
    override fun getItemCount(): Int = differ.currentList.size
    private val diffUtil = object : DiffUtil.ItemCallback<SelectedLocationModel>(){
        override fun areItemsTheSame(oldItem: SelectedLocationModel, newItem: SelectedLocationModel): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: SelectedLocationModel, newItem: SelectedLocationModel): Boolean = oldItem == newItem
    }
    val differ = AsyncListDiffer(this, diffUtil)
}

class SelectedLocationViewHolder(itemView: View, private val onLocationManageListener : OnLocationManageListener? = null) : RecyclerView.ViewHolder(itemView) {
    private val tvRecentSearch : TextView = itemView.findViewById(R.id.tvRecentSearch)
    private val tvDegreeCelsius : TextView = itemView.findViewById(R.id.tvDegreeCelsius)
    private val tvRealFeel : TextView = itemView.findViewById(R.id.tvRealFeel)

    fun bind(selectedLocationModel : SelectedLocationModel){
        tvRecentSearch.text = selectedLocationModel.name
        tvDegreeCelsius.text = selectedLocationModel.realtimeWeather?.current?.temp_c?.toInt().toString()
        val realFeel = "RealFeel: ${selectedLocationModel.realtimeWeather?.current?.feelslike_c}"
        tvRealFeel.text = realFeel
        itemView.setOnClickListener { onLocationManageListener?.onSelect(selectedLocationModel) }
    }
}

interface OnRecentSearchVisibilityListener{
    fun onVisibilityChanged(isVisible : Boolean)
}

interface OnLocationManageListener{
    fun onSelect(searchLocationModel : SelectedLocationModel)
    fun onDeSelect(searchLocationModel : SelectedLocationModel)
}