package com.skyautonet.seda_aiv.ui.splash

import android.util.Log
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.util.RoomDatabaseUtil
import com.skyautonet.seda_aiv.util.SdCardUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    var _progress =  MutableLiveData<Int>().apply {
        value = 0
    }
    var progress: LiveData<Int> = _progress

    var _progressText = MutableLiveData<String>().apply {
        value = "0%"
    }
    var progressText: LiveData<String> = _progressText

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

    fun startLoading(runnable: Runnable) {

        viewModelScope.launch {
            for (i in 0..100) {
                _progress.value = i
                _progressText.value = "${i}%"
                delay(100)
            }
            runnable.run()
        }
    }

}