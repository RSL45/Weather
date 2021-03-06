package com.weather.android.logic.network

import com.weather.android.WeatherApplication
import com.weather.android.logic.model.DailyResponse
import com.weather.android.logic.model.RealTimeResponse
import com.weather.android.logic.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    @GET("v2.5/${WeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng:String,@Path("lat") lat:String) : Call<RealTimeResponse>

    @GET("v2.5/${WeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng:String,@Path("lat") lat:String) : Call<DailyResponse>

    @GET("v2.5/${WeatherApplication.TOKEN}/{lng},{lat}/weather.json")
    fun getWeather(@Path("lng") lng:String,@Path("lat") lat:String) : Call<WeatherResponse>
}