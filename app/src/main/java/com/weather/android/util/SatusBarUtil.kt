package com.weather.android.util

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt


object SatusBarUtil {

    fun setImmersion(window:Window){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            val flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            val flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val attributes: WindowManager.LayoutParams = window.attributes
                attributes.flags = attributes.flags or flagTranslucentNavigation
                window.attributes = attributes
                window.statusBarColor = Color.TRANSPARENT
                window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            } else {
                val attributes: WindowManager.LayoutParams = window.attributes
                attributes.flags =
                    attributes.flags or (flagTranslucentStatus or flagTranslucentNavigation)
                window.attributes = attributes
            }
        }
    }

    fun setStatusTextColor(isDark:Boolean,window: Window,@ColorInt color:Int){
        if (isDark) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //黑色字体
                window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                //设置颜色
                window.statusBarColor = color;
            }
        } else {
            //白色字体
            window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

}