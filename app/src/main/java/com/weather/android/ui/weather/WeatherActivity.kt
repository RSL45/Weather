package com.weather.android.ui.weather

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.activitytest.BaseActivity
import com.weather.android.R
import com.weather.android.WeatherApplication
import com.weather.android.logic.model.ChoosePlaceResponse
import com.weather.android.ui.city.CityManageActivity
import com.weather.android.util.SatusBarUtil
import kotlinx.android.synthetic.main.activity_weather.*
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper

class WeatherActivity : BaseActivity() {

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    lateinit var adapter:WeatherFragmentAdapter

    val choosePlaceList:MutableList<ChoosePlaceResponse> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_weather)

        if (viewModel.locationLng.isEmpty()){
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()){
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()){
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }

        placeName.text = viewModel.placeName

        viewModel.queryAllChoosePlace()

        viewModel.choosePlaceLiveData.observe(this, Observer {response ->
            choosePlaceList.clear()
            if (!WeatherApplication.isFirstBootSaved()) {
                choosePlaceList.add(ChoosePlaceResponse(1,viewModel.placeName,viewModel.locationLng,viewModel.locationLat))
            }else{
                response.let {
                    for (chooosePlaceData in response){
                        if(chooosePlaceData.id.toString() == "1"){
                            choosePlaceList.add(ChoosePlaceResponse(1,viewModel.placeName,viewModel.locationLng,viewModel.locationLat))
                        }else{
                            choosePlaceList.add(ChoosePlaceResponse(chooosePlaceData.id,chooosePlaceData.name,chooosePlaceData.lng,chooosePlaceData.lat))
                        }
                    }

                }
            }
            adapter = WeatherFragmentAdapter(choosePlaceList,this)
            viewPage2.adapter = adapter

        })

        addBtn.setOnClickListener {
            val intent = Intent(this,CityManageActivity::class.java)
            startActivityForResult(intent,1)
        }

        viewPage2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                placeName.text = choosePlaceList[position].name
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.queryAllChoosePlace()
        when(requestCode){
            1 -> if (resultCode == Activity.RESULT_OK){
                val id = data?.getIntExtra("id",0)
                if (id != null) {
                    viewPage2.setCurrentItem(id,false)
                }
            }
        }
    }

}