package com.skyautonet.seda_aiv.data.source

import androidx.lifecycle.LiveData
import com.skyautonet.seda_aiv.model.AlertResponse
import com.skyautonet.seda_aiv.data.Result
import com.skyautonet.seda_aiv.model.VideoListResponse

interface SADataSource {
    fun observeAlerts(): LiveData<Result<AlertResponse>>
    suspend fun getAlerts(): Result<AlertResponse>
    suspend fun refreshAlerts()

    fun observeVideoList(): LiveData<Result<VideoListResponse>>
    suspend fun getVideoList(): Result<VideoListResponse>
    suspend fun refreshVideoList(videoType: Int)
}