package com.weather.android.ui.splash

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.amap.api.location.AMapLocation
import com.example.activitytest.BaseActivity
import com.weather.android.R
import com.weather.android.logic.MapLocationHelper
import com.weather.android.ui.weather.WeatherActivity
import com.weather.android.util.SatusBarUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity : BaseActivity(),MapLocationHelper.LocationCallBack {

    protected var needPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    )

    private val PERMISSON_REQUESTCODE = 0
    private val REQUEST_CODE_LOCATION_SETTINGS = 1

    private var isNeedCheck = true

    private var isCheckNum = 0

    lateinit var helper : MapLocationHelper

    private var lng: String? = null
    private var lat:String? = null
    private var place:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        helper  = MapLocationHelper(this)

        if (!GPSOpen()){
            MaterialDialog(this).show {
                title(R.string.title)
                message(R.string.checkGps)
                cancelable(false)
                cornerRadius(8.0f)
                negativeButton(R.string.refuse){
                    finish()
                }
                positiveButton(R.string.admit) {
                    getLocation()
                }
            }
        }
    }

    private fun checkPermissions(vararg permissions: String) {
        val needRequestPermissonList = findDeniedPermissions(permissions as Array<String>)
        if (null != needRequestPermissonList && needRequestPermissonList.size > 0) {
            ActivityCompat.requestPermissions(this, needRequestPermissonList.toTypedArray(), PERMISSON_REQUESTCODE)
        }else{
            Log.d("SplashActivityTest","checkPermission")
            helper.startMapLocation()
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     */
    private fun findDeniedPermissions(permissions: Array<String>): List<String>? {
        val needRequestPermissonList: MutableList<String> = ArrayList()
        for (perm in permissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                needRequestPermissonList.add(perm)
            }
        }
        return needRequestPermissonList
    }

    /**
     * 检测权限是否已经授权
     */
    private fun verifyPermissions(grantResults: IntArray): Boolean {
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        helper.startMapLocation()
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(grantResults)) {
                isCheckNum++
                showMissingPermissionDialog()
                isNeedCheck = false
            }
        }
    }

    fun getLocation() {
        if (Build.VERSION.SDK_INT >= 26) {
            val i = Intent()
            i.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
            startActivityForResult(i,REQUEST_CODE_LOCATION_SETTINGS)
        }
    }

    private fun GPSOpen(): Boolean {
        val locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // 通过GPS卫星定位,定位级别可以精确到街(通过24颗卫星定位,在室外和空旷的地方定位准确、速度快)
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        // 通过WLAN或移动网络(3G/2G)确定的位置(也称作AGPS,辅助GPS定位。主要用于在室内或遮盖物(建筑群或茂密的深林等)密集的地方定位)
        val network =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gps || network
    }

    private fun showMissingPermissionDialog() {
        MaterialDialog(this).show {
            title(R.string.title)
            message(R.string.checkPermission)
            cancelable(false)
            cornerRadius(8.0f)
            negativeButton(R.string.refuse){
                finish()
            }
            positiveButton(R.string.admit) {
                when(isCheckNum){
                    1 -> checkPermissions(*needPermissions)
                    else -> {
                        startAppSettings()
                        isNeedCheck = true
                    }
                }
            }
        }
    }

    private fun startAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        )
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        if (isNeedCheck) {
            checkPermissions(*needPermissions)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CODE_LOCATION_SETTINGS ->
                if (GPSOpen()){
                    checkPermissions(*needPermissions)
                }else{
                    MaterialDialog(this).show {
                        title(R.string.title)
                        message(R.string.checkGps)
                        cancelable(false)
                        cornerRadius(8.0f)
                        negativeButton(R.string.refuse){
                            finish()
                        }
                        positiveButton(R.string.admit) {
                            getLocation()
                        }
                    }
                }
        }
    }

    override fun onCallLocation(aMapLocation: AMapLocation?) {
        if (aMapLocation != null) {
            lng = aMapLocation.longitude.toString()
            lat = aMapLocation.latitude.toString()
            place = aMapLocation.district+aMapLocation.street
            if (lng!=null&&lat!=null&&place!=null){
                GlobalScope.launch {
                    delay(2000)
                    val intent = Intent(this@SplashActivity, WeatherActivity::class.java).apply {
                        putExtra("location_lng",lng)
                        putExtra("location_lat",lat)
                        putExtra("place_name",place)
                    }
                    startActivity(intent)
                    this@SplashActivity?.finish()
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        helper.stopMapLocation()
    }
}