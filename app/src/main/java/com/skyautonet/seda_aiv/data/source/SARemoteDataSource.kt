package com.skyautonet.seda_aiv.data.source

import androidx.lifecycle.LiveData
import com.skyautonet.seda_aiv.data.ResultObj
import com.skyautonet.seda_aiv.model.*

interface SARemoteDataSource {
    fun observeAlerts(): LiveData<ResultObj<AlertResponse>>
    fun getAlerts(): ResultObj<AlertResponse>
    suspend fun refreshAlerts()

    fun observeVideoList(): LiveData<ResultObj<VideoListResponse>>
    fun getVideoList(): ResultObj<VideoListResponse>
    suspend fun refreshVideoList(videoType: Int)

    fun observeCalibRegi(): LiveData<ResultObj<CalibRegiResponse>>
    fun getCalibRegi(): ResultObj<CalibRegiResponse>
    suspend fun refreshCalibRegi()

    enum class ChannelMode(private val apiValue: Int) {
        FOUR(0), SIX(1), EIGHT(2);

        fun getApiValue(): Int {
            return apiValue
        }
    }

    abstract fun observeSetCameraChannel(): LiveData<ResultObj<SetCameraChannelResponse>>
    abstract suspend fun setCameraChannel(channelMode: SARemoteDataSource.ChannelMode)

    abstract fun observeGetCameraChannel(): LiveData<ResultObj<GetCameraChannelResponse>>
    abstract fun getCameraChannel(): ResultObj<GetCameraChannelResponse>
    abstract suspend fun refreshCameraChannel()
}