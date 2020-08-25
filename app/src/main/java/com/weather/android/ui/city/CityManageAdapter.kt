package com.weather.android.ui.city

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.we.swipe.helper.WeSwipeHelper
import cn.we.swipe.helper.WeSwipeProxyAdapter
import com.weather.android.R
import com.weather.android.logic.model.ChoosePlaceData
import com.weather.android.logic.model.getSky

class CityManageAdapter(private val activity: CityManageActivity) : WeSwipeProxyAdapter<CityManageAdapter.ViewHolder>() {

    private lateinit var deletedItemListener:DeletedItemListener
    private var choosePlaceList: MutableList<ChoosePlaceData> = ArrayList()

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view),WeSwipeHelper.SwipeLayoutTypeCallBack{
        val delete:TextView = view.findViewById(R.id.item_slide)
        val bodyLayout:RelativeLayout = view.findViewById(R.id.bodyLayout)
        val addressNameTv:TextView = view.findViewById(R.id.addressNameTv)
        val locateIv:ImageView = view.findViewById(R.id.locateIv)
        val descriptionTv:TextView = view.findViewById(R.id.descriptionTv)
        val temperatureTv:TextView = view.findViewById(R.id.temperatureTv)


        override fun getSwipeWidth(): Float {
            return delete.width.toFloat()
        }

        override fun onScreenView(): View {
            return bodyLayout
        }

        override fun needSwipeLayout(): View {
            return bodyLayout
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = choosePlaceList[position]
        holder.addressNameTv.text = place.name
        if (position==0){
            holder.locateIv.visibility = View.VISIBLE
            holder.bodyLayout
        }
        holder.descriptionTv.text = "空气${place.description}  ${place.max}°/${place.min}"
        holder.temperatureTv.text = "${place.temperature}°"
        holder.bodyLayout.setOnClickListener {
            //Toast.makeText(activity, place.name, Toast.LENGTH_SHORT).show()
            val intent = Intent()
            intent.putExtra("id",position)
            activity.setResult(Activity.RESULT_OK,intent)
            activity.finish()
        }
        holder.bodyLayout.setBackgroundResource(getSky(place.skycon).cityBg)
        holder.delete.setOnClickListener {
            deletedItemListener.deleted(holder.absoluteAdapterPosition)
        }
    }

    override fun getItemCount() = choosePlaceList.size

    fun setDeletedItemListener(deletedItemListener: DeletedItemListener?) {
        this.deletedItemListener = deletedItemListener!!
    }

    fun setList(list: MutableList<ChoosePlaceData>, refresh: Boolean) {
        if (refresh) {
            choosePlaceList.clear()
        }
        choosePlaceList.addAll(list)
        proxyNotifyDataSetChanged()
    }

    fun getList() = choosePlaceList

    fun removeDataByPosition(position: Int) {
        if (position >= 0 && position < choosePlaceList.size) {
            choosePlaceList.removeAt(position)
            proxyNotifyItemRemoved(position)
            proxyNotifyDataSetChanged()
        }
    }

    interface DeletedItemListener {
        fun deleted(position: Int)
    }

}


