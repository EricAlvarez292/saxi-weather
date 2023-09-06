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

class SearchLocationAdapter(private val onLocationSearchListener : OnLocationSearchListener? = null ) : RecyclerView.Adapter<SearchGeoLocationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchGeoLocationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_search_location, parent, false)
        return SearchGeoLocationViewHolder(view, onLocationSearchListener)
    }
    override fun onBindViewHolder(holder: SearchGeoLocationViewHolder, position: Int) { holder.bind(differ.currentList[position]) }
    override fun getItemCount(): Int = differ.currentList.size

    private val diffUtil = object : DiffUtil.ItemCallback<SearchLocationModel>(){
        override fun areItemsTheSame(
            oldItem: SearchLocationModel,
            newItem: SearchLocationModel
        ): Boolean = oldItem == newItem
        override fun areContentsTheSame(
            oldItem: SearchLocationModel,
            newItem: SearchLocationModel
        ): Boolean = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffUtil)
}
class SearchGeoLocationViewHolder(itemView: View, private val onLocationSearchListener : OnLocationSearchListener? = null ) : RecyclerView.ViewHolder(itemView) {
    private val tvSearch : TextView = itemView.findViewById(R.id.tvSearch)
    fun bind(searchLocationModel : SearchLocationModel){
        "${searchLocationModel.name}, ${searchLocationModel.region}".let { tvSearch.text = it }
        itemView.setOnClickListener { onLocationSearchListener?.onSelectSearch(searchLocationModel) }
    }
}

interface OnLocationSearchListener{
    fun onSelectSearch(searchLocationModel : SearchLocationModel)
}
