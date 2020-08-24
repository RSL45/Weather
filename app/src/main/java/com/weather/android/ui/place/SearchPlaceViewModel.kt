package com.weather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.android.logic.ApiRepository
import com.weather.android.logic.ChoosePlaceRepository
import com.weather.android.logic.model.ChoosePlaceData
import com.weather.android.logic.model.Location
import com.weather.android.logic.model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchPlaceViewModel :ViewModel(){

    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    val placeLiveData = Transformations.switchMap(searchLiveData){query ->
        ApiRepository.searchPlaces(query)
    }

    fun searchPlaces(query:String){
        searchLiveData.value = query
    }

    private val locationLiveData = MutableLiveData<Location>()

    val weatherLivedata = Transformations.switchMap(locationLiveData){location ->
        ApiRepository.refreshCityWeather(location.lng,location.lat)
    }

    fun loadhWeather(lng:String,lat:String){
        locationLiveData.value = Location(lng,lat)
    }

    val choosePlaceInsertResult = MutableLiveData<Long?>()

    fun insertChoosePlace(choosePlaceData: ChoosePlaceData){
        viewModelScope.launch {
            choosePlaceInsertResult.value = ChoosePlaceRepository.insertChoosePlace(choosePlaceData)
        }
    }

}