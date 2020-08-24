package com.weather.android.logic

import android.content.Context
import android.util.Log
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.weather.android.WeatherApplication

//高德定位
class MapLocationHelper(locationCallback: LocationCallBack) : AMapLocationListener {

    private val mLocationClient : AMapLocationClient
    private val mLocationOption : AMapLocationClientOption
    private var mLocationCallBack : LocationCallBack

    init {
        Log.d("Helper","响应了")
        mLocationCallBack = locationCallback
        mLocationClient = AMapLocationClient(WeatherApplication.context)
        mLocationOption = AMapLocationClientOption()
        // 设置定位监听
        mLocationClient.setLocationListener(this)
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //设置单次定位
        mLocationOption.isOnceLocation = true
        //设置定位同时返回地址描述
        mLocationOption.isNeedAddress = true
        // 设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
    }

    //开启定位
    fun startMapLocation(){
        /*if (!mLocationClient.isStarted) */
        mLocationClient.startLocation()
    }

    fun stopMapLocation(){
        if (mLocationClient.isStarted) mLocationClient.stopLocation()
    }

    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        Log.d("MapLocationHelper","1")
        if (aMapLocation != null) {
            Log.d("MapLocationHelper","2")
            if (aMapLocation.errorCode == 0) {
                // 可在其中解析amapLocation获取相应内容。
                //给接口设置数据
                mLocationCallBack.onCallLocation(aMapLocation);
                stopMapLocation();
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                stopMapLocation();
            }
        } else {
            stopMapLocation();
        }
    }

    interface LocationCallBack{
        fun onCallLocation(aMapLocation: AMapLocation?)
    }

    fun setLocationCallBack(locationCallBack: LocationCallBack){
        mLocationCallBack = locationCallBack
    }
}