package com.weather.android.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.android.logic.ApiRepository
import com.weather.android.logic.ChoosePlaceRepository
import com.weather.android.logic.model.ChoosePlaceData
import com.weather.android.logic.model.Location
import kotlinx.coroutines.launch

class WeatherFragmentViewModel : ViewModel() {


    private val locationLiveData = MutableLiveData<Location>()

    var locationLng = ""
    var locationLat = ""
    var placeName = ""
    var id = ""

    val weatherLivedata = Transformations.switchMap(locationLiveData){ location ->
        ApiRepository.refreshCityWeather(location.lng,location.lat)
    }

    fun refreshWeather(lng:String,lat:String){
        locationLiveData.value = Location(lng,lat)
    }

    fun insertChoosePlace(choosePlaceData: ChoosePlaceData){
        viewModelScope.launch {
            ChoosePlaceRepository.insertChoosePlace(choosePlaceData)
        }
    }

    fun updateChoosePlace(name : String,skycon:String,description : String,temperature : Int,max : Int,min : Int){
        viewModelScope.launch {
            ChoosePlaceRepository.updateChoosePlace(name,skycon,description,temperature,max,min)
        }
    }

    fun updateLocatePlace(name : String,lng:String,lat:String,skycon:String,description : String,temperature : Int,max : Int,min : Int){
        viewModelScope.launch {
            ChoosePlaceRepository.updateLocatePlace(name,lng,lat,skycon,description,temperature,max,min)
        }
    }
}