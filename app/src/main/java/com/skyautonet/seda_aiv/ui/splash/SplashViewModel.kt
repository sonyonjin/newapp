package com.skyautonet.seda_aiv.ui.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.data.source.DefaultSARepository
import com.skyautonet.seda_aiv.data.source.FakeSALocalDataSource
import com.skyautonet.seda_aiv.data.source.FakeSARemoteDataSource
import com.skyautonet.seda_aiv.ui.BaseViewModel
import com.skyautonet.seda_aiv.util.RoomDatabaseUtil
import com.skyautonet.seda_aiv.util.SdCardUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : BaseViewModel() {
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

            // config.txt저장된 내용을 읽어서 baseUrl로 사용해야 하므로 permission획득후 saRepository을 사용할 수 있도록 inject시킴
            SAApp.saRepository = DefaultSARepository(FakeSARemoteDataSource, FakeSALocalDataSource)
            SAApp.instance.getNetComponent().inject(SAApp.saRepository)
            SAApp.instance.getNetComponent().inject(this)
        } else {
            Log.e("SeDA_AIV", errorMsg)
        }
    }

    fun startLoading(runnable: Runnable) {

        viewModelScope.launch {
            for (i in 0..100) {
                _progress.value = i
                _progressText.value = "${i}%"
                delay(10)
            }
            runnable.run()
        }
    }

}