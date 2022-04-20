package com.skyautonet.seda_aiv.ui.setting.calibration

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.data.source.SARemoteDataSource
import com.skyautonet.seda_aiv.data.source.SARepository
import com.skyautonet.seda_aiv.ui.BaseViewModel

class CameraChannelViewModel (
    private val saRepository: SARepository = SAApp.saRepository,
) : BaseViewModel() {

    var isVisibleCameraChannel4 = MutableLiveData(ObservableBoolean(false))
    var isVisibleCameraChannel6 = MutableLiveData(ObservableBoolean(false))
    var isVisibleCameraChannel8 = MutableLiveData(ObservableBoolean(false))

    fun setVisibleCameraChannel(channelMode: SARemoteDataSource.ChannelMode) {
        when (channelMode) {
            SARemoteDataSource.ChannelMode.FOUR -> {
                isVisibleCameraChannel4.value = ObservableBoolean(true)
            }
            SARemoteDataSource.ChannelMode.SIX -> {
                isVisibleCameraChannel6.value = ObservableBoolean(true)
            }
            SARemoteDataSource.ChannelMode.EIGHT -> {
                isVisibleCameraChannel8.value = ObservableBoolean(true)
            }
        }
    }
}