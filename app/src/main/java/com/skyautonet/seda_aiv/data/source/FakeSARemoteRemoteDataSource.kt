package com.skyautonet.seda_aiv.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.skyautonet.seda_aiv.data.ResultObj
import com.skyautonet.seda_aiv.model.*

object FakeSARemoteDataSource : SARemoteDataSource {

    private val ALERT_DEFAULT_DATA = AlertResponse().apply {
        add(AlertItem("LEFT_OFF"))
        add(AlertItem("RIGHT_OFF"))
        add(AlertItem("TOP_OFF"))
        add(AlertItem("BOTTOM_OFF"))
    }

    private val observableAlerts = MutableLiveData<ResultObj<AlertResponse>>()

    override fun observeAlerts(): LiveData<ResultObj<AlertResponse>> {
        return observableAlerts
    }

    override fun getAlerts(): ResultObj<AlertResponse> {
        return observableAlerts.value ?: ResultObj.Success(ALERT_DEFAULT_DATA)
    }

    override suspend fun refreshAlerts() {
    }

    private val observableVideoList = MutableLiveData<ResultObj<VideoListResponse>>()

    override fun observeVideoList(): LiveData<ResultObj<VideoListResponse>> {
        return observableVideoList
    }

    override fun getVideoList(): ResultObj<VideoListResponse> {
        return observableVideoList.value ?: ResultObj.Success(VideoListResponse())
    }

    override suspend fun refreshVideoList(videoType: Int) {
    }

    private val observableCalibRegi = MutableLiveData<ResultObj<CalibRegiResponse>>()

    override fun observeCalibRegi(): LiveData<ResultObj<CalibRegiResponse>> {
        return observableCalibRegi
    }

    override fun getCalibRegi(): ResultObj<CalibRegiResponse> {
        return observableCalibRegi.value ?: ResultObj.Success(CalibRegiResponse(false))
    }

    override suspend fun refreshCalibRegi() {
    }

    private val observableSetCameraChannel = MutableLiveData<ResultObj<SetCameraChannelResponse>>()

    override fun observeSetCameraChannel(): LiveData<ResultObj<SetCameraChannelResponse>> {
        return observableSetCameraChannel
    }

    override suspend fun setCameraChannel(channelMode: SARemoteDataSource.ChannelMode) {
    }

    private val observableGetCameraChannel = MutableLiveData<ResultObj<GetCameraChannelResponse>>()

    override fun observeGetCameraChannel(): LiveData<ResultObj<GetCameraChannelResponse>> {
        return observableGetCameraChannel
    }

    override fun getCameraChannel(): ResultObj<GetCameraChannelResponse> {
        return observableGetCameraChannel.value ?: ResultObj.Success(GetCameraChannelResponse(4))
    }

    override suspend fun refreshCameraChannel() {
    }

}