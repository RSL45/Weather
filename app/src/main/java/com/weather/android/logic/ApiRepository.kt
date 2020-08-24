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
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext
import kotlin.math.ln

object ApiRepository {

    /*查询地址*/
    fun searchPlaces(query:String) = fire(Dispatchers.IO){
        val placeResponse = WeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok"){
            val places = placeResponse.places
            Result.success(places)
        }else{
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }

    }

    fun refreshCityWeather(lng: String,lat: String) = fire(Dispatchers.IO){
        val weatherResponse = WeatherNetwork.getWeather(lng,lat)
        if (weatherResponse.status == "ok"){
            Result.success(weatherResponse)
        }else{
            Result.failure(RuntimeException("weather response status is ${weatherResponse.status}"))
        }
    }



    private fun <T> fire(context: CoroutineContext,block:suspend ()->Result<T>) = liveData<Result<T>>(context) {
        val result = try {
            block()
        }catch (e:Exception){
            Result.failure<T>(e)
        }
        emit(result)
    }
}