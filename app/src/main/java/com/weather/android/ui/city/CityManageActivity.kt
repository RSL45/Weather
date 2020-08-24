package com.weather.android.ui.city

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.we.swipe.helper.WeSwipe
import com.afollestad.materialdialogs.MaterialDialog
import com.example.activitytest.BaseActivity
import com.weather.android.R
import com.weather.android.logic.model.ChoosePlaceData
import com.weather.android.ui.place.SearchPlaceActivity
import com.weather.android.util.SatusBarUtil
import kotlinx.android.synthetic.main.activity_city_manage.*
import kotlinx.android.synthetic.main.activity_city_manage.recyclerView


class CityManageActivity : BaseActivity(),CityAdapter.DeletedItemListener {

    val viewModel by lazy { ViewModelProvider(this).get(CityManageViewModel::class.java) }

    private lateinit var adapter: CityAdapter

    var choosePlaceList:MutableList<ChoosePlaceData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_city_manage)

        SatusBarUtil.setImmersion(window)

        SatusBarUtil.setStatusTextColor(true,window,Color.TRANSPARENT)

        searchTv.setOnClickListener {
            val intent = Intent(this,SearchPlaceActivity::class.java)
            startActivity(intent)
        }

        backBtn.setOnClickListener{
            finish()
        }

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = CityAdapter(this)
        adapter.setDeletedItemListener(this)
        recyclerView.adapter = adapter

        val attach:WeSwipe = WeSwipe.attach(recyclerView)
        adapter.setWeSwipe(attach)

        viewModel.queryAllChoosePlace()
        viewModel.choosePlaceLiveData.observe(this, Observer {response ->
            choosePlaceList.clear()
            choosePlaceList = response
            Log.d("CityManageActivity",choosePlaceList.toString())
            for (i in response){
                Log.d("CityManageActivity",i.id.toString())
            }
            response?.let {
                adapter.setList(response,true)
            }
        })


        searchTv.setOnClickListener {
            val intent = Intent(this,SearchPlaceActivity::class.java)
            startActivityForResult(intent,1)
        }
    }

    override fun deleted(position: Int) {
        if (position==0){
            MaterialDialog(this).show {
                title(R.string.title)
                message(R.string.locate_city)
                cornerRadius(8.0f)
                negativeButton(R.string.cancel)
            }
        }else {
            MaterialDialog(this).show {
                title(R.string.title)
                message(R.string.delete_city)
                cornerRadius(8.0f)
                negativeButton(R.string.cancel)
                positiveButton(R.string.delete) {
                    viewModel.deleteChoosePlaceByName(choosePlaceList[position])
                    choosePlaceList.remove(choosePlaceList[position])
                    adapter.removeDataByPosition(position)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1 -> if (resultCode == Activity.RESULT_OK){
                val refresh = data?.getBooleanExtra("refresh",false)
                if (refresh!!) viewModel.queryAllChoosePlace()
            }
        }
    }
}