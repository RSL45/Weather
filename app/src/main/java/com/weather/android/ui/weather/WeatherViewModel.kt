package com.weather.android.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.android.logic.ApiRepository
import com.weather.android.logic.ChoosePlaceRepository
import com.weather.android.logic.model.ChoosePlaceData
import com.weather.android.logic.model.Location
import com.weather.android.logic.model.WeatherResponse
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    var locationLng = ""
    var locationLat = ""
    var placeName = ""

    val choosePlaceLiveData = MutableLiveData<MutableList<ChoosePlaceData>>()

    fun queryAllChoosePlace(){
        viewModelScope.launch {
            choosePlaceLiveData.value = ChoosePlaceRepository.queryAllChoosePlace()
        }
    }

    /*fun refreshWeather(lng:String,lat:String){
        locationLiveData.value = Location(lng,lat)
    }*/

    private val locationLiveData = MutableLiveData<Location>()

    val weatherLivedata = Transformations.switchMap(locationLiveData){location ->
        ApiRepository.refreshCityWeather(location.lng,location.lat)
    }

    fun loadWeather(lng:String,lat:String){
        locationLiveData.value = Location(lng,lat)
    }


    fun loadAllChoosePlaceWeather(list: MutableList<ChoosePlaceData>){
        for (i in list){
            locationLiveData.value = Location(i.lng,i.lat)
        }
    }

    fun insertChoosePlace(choosePlaceData: ChoosePlaceData){
        viewModelScope.launch {
            ChoosePlaceRepository.insertChoosePlace(choosePlaceData)
        }
    }

    fun updateChoosePlace(name : String,skycon:String,description : String,temperature : Int,max : Int,min : Int){
        ChoosePlaceRepository.updateChoosePlace(name,skycon,description,temperature,max,min)
    }

    fun updateLocatePlace(name : String,lng:String,lat:String,skycon:String,description : String,temperature : Int,max : Int,min : Int){
        ChoosePlaceRepository.updateLocatePlace(name,lng,lat,skycon,description,temperature,max,min)
    }
}