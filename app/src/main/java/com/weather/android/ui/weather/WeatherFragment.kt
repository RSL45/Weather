package com.weather.android.ui.weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.weather.android.R
import com.weather.android.WeatherApplication
import com.weather.android.logic.model.ChoosePlaceData
import com.weather.android.logic.model.WeatherResponse
import com.weather.android.logic.model.getSky
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.android.synthetic.main.life_index.*
import kotlinx.android.synthetic.main.now.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherFragment :Fragment() {

    val viewModel by lazy { ViewModelProvider(this).get(WeatherFragmentViewModel::class.java) }

    companion object {
        fun newInstance(id:Long,placeName: String, lng: String, lat: String): WeatherFragment {
            val fragment = WeatherFragment()
            val bundle = Bundle()
            bundle.putLong("id",id)
            bundle.putString("placeName", placeName)
            bundle.putString("lng", lng)
            bundle.putString("lat", lat)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (arguments != null) {
            viewModel.id = arguments!!.get("id").toString()
            viewModel.placeName = arguments!!.get("placeName").toString()
            viewModel.locationLng = arguments!!.getString("lng").toString()
            viewModel.locationLat = arguments!!.getString("lat").toString()
        }

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        refreshWeather()
        swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }

        viewModel.weatherLivedata.observe(viewLifecycleOwner, Observer { result ->
            val weatherResponse = result.getOrNull()
            val weather = weatherResponse?.result
            if (weather != null) {
                val choosePlaceData = ChoosePlaceData(
                    viewModel.placeName,
                    viewModel.locationLng,
                    viewModel.locationLat,
                    weather.realtime.skycon,
                    weather.realtime.airQuality.description.chn,
                    weather.realtime.temperature.toInt(),
                    weather.daily.temperature[0].max.toInt(),
                    weather.daily.temperature[0].min.toInt()
                )
                if (viewModel.id == "1") {
                    if (!WeatherApplication.isFirstBootSaved()) {
                        viewModel.insertChoosePlace(choosePlaceData)
                        WeatherApplication.saveFirstBoot()
                    } else {
                        viewModel.updateLocatePlace(
                            viewModel.placeName,
                            viewModel.locationLng,
                            viewModel.locationLat,
                            weather.realtime.skycon,
                            weather.realtime.airQuality.description.chn,
                            weather.realtime.temperature.toInt(),
                            weather.daily.temperature[0].max.toInt(),
                            weather.daily.temperature[0].min.toInt()
                        )
                    }
                } else {
                    viewModel.updateChoosePlace(
                        viewModel.placeName,
                        weather.realtime.skycon,
                        weather.realtime.airQuality.description.chn,
                        weather.realtime.temperature.toInt(),
                        weather.daily.temperature[0].max.toInt(),
                        weather.daily.temperature[0].min.toInt()
                    )
                }
                showWeatherInfo(weather)
            }
            swipeRefresh.isRefreshing = false
        })
    }

    fun refreshWeather(){
        viewModel.refreshWeather(viewModel.locationLng,viewModel.locationLat)
        swipeRefresh.isRefreshing = true
    }

    private fun showWeatherInfo(weather: WeatherResponse.Result) {
        val realtime = weather.realtime
        val daily = weather.daily

        //now布局
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        currentTemp.text = currentTempText
        currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        currentAQI.text = currentPM25Text
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        // forecast布局
        forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(context).inflate(R.layout.forecast_item, forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            forecastLayout.addView(view)
        }
        // 填充life_index.xml布局中的数据
        val lifeIndex = daily.lifeIndex
        coldRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        carWashingText.text = lifeIndex.carWashing[0].desc
        weatherLayout.visibility = View.VISIBLE
    }


}