package com.skyautonet.seda_aiv.data.source

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.skyautonet.seda_aiv.common.CommonUtils
import com.skyautonet.seda_aiv.common.SharedPref
import com.skyautonet.seda_aiv.rest.SAAppInterface
import com.skyautonet.seda_aiv.data.ResultObj
import com.skyautonet.seda_aiv.data.source.local.file.VideoFile
import com.skyautonet.seda_aiv.model.*
import retrofit2.Retrofit
import javax.inject.Inject

abstract class SARepository {
    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var commonUtils: CommonUtils
    @Inject
    lateinit var sharedPref: SharedPref
    @Inject
    lateinit var retrofit: Retrofit
    @Inject
    lateinit var saAppInterface: SAAppInterface

    abstract fun observeAlerts(): LiveData<ResultObj<AlertResponse>>
    abstract fun getAlerts(): ResultObj<AlertResponse>
    abstract suspend fun refreshAlerts()

    abstract fun observeVideoList(): LiveData<ResultObj<VideoListResponse>>
    abstract fun getVideoList(): ResultObj<VideoListResponse>
    abstract suspend fun refreshVideoList(videoType: Int)

    abstract fun observeVideoStorage(): LiveData<MutableList<VideoFile>>
    abstract fun getVideoStorage(): MutableList<VideoFile>
    abstract suspend fun refreshVideoStorage()
    abstract suspend fun deleteVideoFile(videoList: MutableList<VideoFile>)

    abstract fun observeCalibRegi(): LiveData<ResultObj<CalibRegiResponse>>
    abstract fun getCalibRegi(): ResultObj<CalibRegiResponse>
    abstract suspend fun refreshCalibRegi()

    abstract fun observeSetCameraChannel(): LiveData<ResultObj<SetCameraChannelResponse>>
    abstract suspend fun setCameraChannel(channelMode: SARemoteDataSource.ChannelMode)

    abstract fun observeGetCameraChannel(): LiveData<ResultObj<GetCameraChannelResponse>>
    abstract fun getCameraChannel(): ResultObj<GetCameraChannelResponse>
    abstract suspend fun refreshCameraChannel()

}