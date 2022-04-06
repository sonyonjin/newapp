package com.skyautonet.seda_aiv.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.skyautonet.seda_aiv.model.AlertResponse
import com.skyautonet.seda_aiv.model.AlertItem
import com.skyautonet.seda_aiv.data.Result
import com.skyautonet.seda_aiv.model.VideoListResponse

object FakeSARemoteDataSource : SADataSource {

    private val ALERT_DEFAULT_DATA = AlertResponse().apply {
        add(AlertItem("LEFT_OFF"))
        add(AlertItem("RIGHT_OFF"))
        add(AlertItem("TOP_OFF"))
        add(AlertItem("BOTTOM_OFF"))
    }

    private val observableAlerts = MutableLiveData<Result<AlertResponse>>()
    private val observableVideoList = MutableLiveData<Result<VideoListResponse>>()

    override fun observeAlerts(): LiveData<Result<AlertResponse>> {
        return observableAlerts
    }

    override suspend fun getAlerts(): Result<AlertResponse> {
        return observableAlerts.value ?: Result.Success(ALERT_DEFAULT_DATA)
    }

    override suspend fun refreshAlerts() {
    }

    override fun observeVideoList(): LiveData<Result<VideoListResponse>> {
        return observableVideoList
    }

    override suspend fun getVideoList(): Result<VideoListResponse> {
        return observableVideoList.value ?: Result.Success(VideoListResponse())
    }

    override suspend fun refreshVideoList(videoType: Int) {
    }

}