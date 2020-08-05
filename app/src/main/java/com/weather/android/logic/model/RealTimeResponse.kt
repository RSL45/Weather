package com.weather.android.logic.model

import com.google.gson.annotations.SerializedName

/*实况天气*/
data class RealTimeResponse(val status:String,val result:Result){

    data class Result(val realtime:Realtime)

    /*skycon:主要天气现象
    * temperature:温度
    * airQuality:空气质量指数*/
    data class Realtime(val skycon:String,val temperature:Float,@SerializedName("air_quality") val airQuality:AirQuality)

    data class AirQuality(val aqi:AQI)

    /*chn：中国标准*/
    data class AQI(val chn:Float)
}