package com.weather.android.logic.model

import com.google.gson.annotations.SerializedName

/*通用预报*/
data class WeatherResponse(val status:String,val result:Result){

    data class Result(val realtime: RealTimeResponse.Realtime,val daily:DailyResponse.Daily)

}