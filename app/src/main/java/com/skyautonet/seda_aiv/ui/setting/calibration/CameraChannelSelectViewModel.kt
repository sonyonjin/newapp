package com.skyautonet.seda_aiv.ui.setting.calibration

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.data.ResultObj
import com.skyautonet.seda_aiv.data.source.SARemoteDataSource
import com.skyautonet.seda_aiv.data.source.SARepository
import com.skyautonet.seda_aiv.model.GetCameraChannelResponse
import com.skyautonet.seda_aiv.model.SetCameraChannelResponse
import com.skyautonet.seda_aiv.ui.BaseViewModel
import kotlinx.coroutines.launch

class CameraChannelSelectViewModel (
    private val saRepository: SARepository = SAApp.saRepository,
) : BaseViewModel() {

    private var _getCameraChannelResponse = saRepository.observeGetCameraChannel()
    val getCameraChannelResponse: LiveData<ResultObj<GetCameraChannelResponse>>
        get() = _getCameraChannelResponse

    private var _setCameraChannelResponse = saRepository.observeSetCameraChannel()
    val setCameraChannelResponse: LiveData<ResultObj<SetCameraChannelResponse>>
        get() = _setCameraChannelResponse

    fun getCameraChannel() {
        viewModelScope.launch {
            saRepository.refreshCameraChannel()
        }
    }

    private var channelMode = SARemoteDataSource.ChannelMode.FOUR

    fun getChannelMode(): SARemoteDataSource.ChannelMode {
        return channelMode
    }

    fun setChannelMode(channelMode: SARemoteDataSource.ChannelMode) {
        this.channelMode = channelMode
        setCameraChannel()
    }

    fun setCameraChannel() {
        viewModelScope.launch {
            saRepository.setCameraChannel(channelMode)
        }
    }

}