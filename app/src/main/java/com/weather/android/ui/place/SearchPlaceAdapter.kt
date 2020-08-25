package com.weather.android.ui.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.weather.android.R
import com.weather.android.logic.model.Place

class SearchPlaceAdapter (private val activity: SearchPlaceActivity ,private val placeList: ArrayList<Place>) : RecyclerView.Adapter<SearchPlaceAdapter.ViewHolder>(){

    private lateinit var onclickItemListener : OnClickItemListener

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val placeName : TextView = view.findViewById(R.id.placeName)
        val placeAddress : TextView = view.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition
            val place = placeList[position]
            onclickItemListener.onClick(position)
        }
        return holder
    }

    override fun getItemCount() = placeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    fun setonClickItemListener(onclickItemListener: OnClickItemListener) {
        this.onclickItemListener = onclickItemListener
    }

    interface OnClickItemListener{
        fun onClick(position:Int)
    }
}