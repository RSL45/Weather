package com.weather.android.logic.model

import com.google.gson.annotations.SerializedName

/*实况天气*/
data class RealTimeResponse(val status:String,val result:Result){

    data class Result(val realtime:Realtime)

    /*skycon:主要天气现象
    * temperature:温度
    * airQuality:空气质量指数*/
    data class Realtime(val skycon:String, val temperature:Float,val humidity:Float,val cloudrate:Float,val visibility:Float,@SerializedName("apparent_temperature") val apparentTemperature:Float,@SerializedName("air_quality") val airQuality:AirQuality, @SerializedName("life_index") val lifeIndex:LifeIndex)

    data class AirQuality(val pm25:Float,val pm10: Float,val o3:Float,val so2:Float,val no2:Float,val co:Float,val aqi: AQI, val description: Description)

    data class AQI(val chn:Float)

    data class Description(val chn:String)

    data class LifeIndex(val ultraviolet:Ultraviolet,val comfort:Comfort)

    data class Ultraviolet(val index:Float,val desc:String)

    data class Comfort(val index: Float,val desc: String)
}