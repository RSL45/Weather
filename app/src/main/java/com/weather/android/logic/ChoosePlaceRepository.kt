package com.weather.android.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.weather.android.logic.model.ChoosePlaceData
import com.weather.android.logic.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object ChoosePlaceRepository {

    suspend fun queryAllChoosePlace() = withContext(Dispatchers.IO){
        RoomHelper.queryAllChoosePlace()
    }

    suspend fun insertChoosePlace(choosePlaceData: ChoosePlaceData): Long? = withContext(Dispatchers.IO){
        RoomHelper.insertChoosePlace(choosePlaceData)
    }

    suspend fun updateChoosePlace(name : String,skycon:String,description : String,temperature : Int,max : Int,min : Int) = withContext(Dispatchers.IO){
        RoomHelper.updateChoosePlace(name,skycon,description,temperature,max,min)
    }

    suspend fun updateLocatePlace(name : String,lng:String,lat:String,skycon:String,description : String,temperature : Int,max : Int,min : Int) = withContext(Dispatchers.IO){
        RoomHelper.updateLocatePlace(name,lng,lat,skycon,description,temperature,max,min)
    }

    suspend fun deleteChoosePlaceByName(name:String) = withContext(Dispatchers.IO){
        RoomHelper.deleteChoosePlaceByName(name)
    }
}