package com.skyautonet.seda_aiv.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.util.RoomDatabaseUtil
import com.skyautonet.seda_aiv.util.SdCardUtil

class SplashViewModel : ViewModel() {

    fun setConfigFile() {
        val errorMsg = SdCardUtil.setConfigFile(SAApp.instance)
        if (errorMsg == null) {
            val configData = RoomDatabaseUtil.getApplicationConfigData()
            Log.i("SeDA_AIV", configData.ssid)
            Log.i("SeDA_AIV", configData.password)
            Log.i("SeDA_AIV", configData.regNo)
            Log.i("SeDA_AIV", configData.rtspLink1)
            Log.i("SeDA_AIV", configData.rtspLink2)
            Log.i("SeDA_AIV", configData.rtspLink3)
            Log.i("SeDA_AIV", configData.ipAddress)
        } else {
            Log.e("SeDA_AIV", errorMsg)
        }
    }

}