package com.weather.android.logic

import com.weather.android.WeatherApplication
import com.weather.android.logic.database.ChoosePlaceDatabase
import com.weather.android.logic.model.ChoosePlaceData

object RoomHelper {
    private val choosePlaceDatabase by lazy {
        ChoosePlaceDatabase.getDatabse(WeatherApplication.context)
    }

    private val choosePlaceDao by lazy{
        choosePlaceDatabase?.choosePlaceDao()
    }

    suspend fun queryAllChoosePlace() : MutableList<ChoosePlaceData>{
        val response = choosePlaceDao?.queryAllPlace()?.toMutableList()
        return response
    }

    suspend fun queryFirstChoosePlace() : ChoosePlaceData{
        val response = choosePlaceDao?.queryAllPlace().toList().get(0)
        return response
    }

    suspend fun insertChoosePlace(choosePlaceData: ChoosePlaceData):Long? =
        choosePlaceDao?.let {
            it.queryChoosePlaceByName(choosePlaceData.name)?.let {
                var i = choosePlaceDao.deleteChoosePlace(it)
            }
            it.insertPlace(choosePlaceData)
        }

    suspend fun updateChoosePlace(name : String,skycon:String,description : String,temperature : Int,max : Int,min : Int){
        choosePlaceDao?.updateChoosePlace(name,skycon,description,temperature,max,min)
    }

    suspend fun updateLocatePlace(name : String,lng:String,lat:String,skycon:String,description : String,temperature : Int,max : Int,min : Int){
        choosePlaceDao?.updateLocatePlace(name,lng,lat,skycon,description,temperature,max,min)
    }


    suspend fun deleteChoosePlace(choosePlaceData: ChoosePlaceData){
        choosePlaceDao?.deleteChoosePlace(choosePlaceData)
    }


    suspend fun deleteChoosePlaceByName(choosePlaceData: String){
        choosePlaceDao?.deleteChoosePlaceByName(choosePlaceData)
    }
}