package com.weather.android.logic

import android.content.Context
import android.util.Log
import androidx.lifecycle.liveData
import com.weather.android.logic.dao.PlaceDao
import com.weather.android.logic.model.Place
import com.weather.android.logic.model.Weather
import com.weather.android.logic.network.WeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext
import kotlin.math.ln

object Repository {

    /*查询地址*/
    //block: suspend LiveDataScope<T>.() -> Unit 提供挂起函数的上下文
    fun searchPlaces(query:String) = fire(Dispatchers.IO){
        val placeResponse = WeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok"){
            val places = placeResponse.places
            Result.success(places)
        }else{
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }

    }

    /*获取天气*/
    fun refreshWeather(lng:String,lat:String) = fire(Dispatchers.IO){
        coroutineScope {
            val deferredRealtime = async {
                WeatherNetwork.getRealtimeWeather(lng,lat)
            }
            val deferredDaily = async {
                WeatherNetwork.getDailyWeather(lng,lat)
            }
            val realTimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if (realTimeResponse.status=="ok" && dailyResponse.status=="ok"){
                val weather = Weather(realTimeResponse.result.realtime,dailyResponse.result.daily)
                Result.success(weather)
            }else{
                Result.failure(RuntimeException("realtime response status is ${realTimeResponse.status}" +
                        "daily response status is ${dailyResponse.status}"))
            }
        }
    }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlacedSaved() = PlaceDao.isPlaceSaved()

    private fun <T> fire(context: CoroutineContext,block:suspend ()->Result<T>) = liveData<Result<T>>(context) {
        val result = try {
            block()
        }catch (e:Exception){
            Result.failure<T>(e)
        }
        emit(result)
    }
}