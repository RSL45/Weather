package com.weather.android.ui.place

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.activitytest.BaseActivity
import com.weather.android.R
import com.weather.android.logic.model.ChoosePlaceData
import com.weather.android.logic.model.Place
import com.weather.android.util.SatusBarUtil
import kotlinx.android.synthetic.main.activity_search_place.*

class SearchPlaceActivity : BaseActivity(),SearchPlaceAdapter.OnClickItemListener {

    val viewModel by lazy { ViewModelProvider(this).get(SearchPlaceViewModel::class.java) }

    private lateinit var adapter: SearchPlaceAdapter

    private lateinit var place:Place

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_place)

        //配置RecyclerView
        val layoutManager = LinearLayoutManager(this)
        searchRv.layoutManager = layoutManager
        adapter = SearchPlaceAdapter(this,viewModel.placeList)
        adapter.setonClickItemListener(this)
        searchRv.adapter = adapter


        //监控输入框变化内容
        searchEdit.addTextChangedListener {editable ->
            val content = editable.toString()
            if (content.isNotEmpty()){
                viewModel.searchPlaces(content)
            }else{
                searchRv.visibility = View.GONE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        //监听searchLiveData变化
        viewModel.placeLiveData.observe(this, Observer {result ->
            val places = result.getOrNull()
            Log.d("SearchPlaceActivity1",places.toString())
            places?.let {
                searchRv.visibility = View.VISIBLE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                Log.d("SearchPlaceActivity2",viewModel.placeList.size.toString())
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.weatherLivedata.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather!=null){
                val choosePlaceData = ChoosePlaceData(
                    place.name,
                    place.location.lng,
                    place.location.lat,
                    weather.result.realtime.skycon,
                    weather.result.realtime.airQuality.description.chn,
                    weather.result.realtime.temperature.toInt(),
                    weather.result.daily.temperature[0].max.toInt(),
                    weather.result.daily.temperature[0].min.toInt()
                )
                viewModel.insertChoosePlace(choosePlaceData)
            }else{
                result.exceptionOrNull()?.printStackTrace()
            }
        })

        viewModel.choosePlaceInsertResult.observe(this, Observer {
            val intent = Intent()
            intent.putExtra("refresh",true)
            setResult(Activity.RESULT_OK,intent)
            finish()
        })

        cancelBtn.setOnClickListener {
            finish()
        }
    }

    override fun onClick(position: Int) {
        place = viewModel.placeList[position]
        viewModel.loadhWeather(place.location.lng,place.location.lat)
    }
}