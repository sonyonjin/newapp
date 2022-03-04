package com.skyautonet.seda_aiv.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.skyautonet.seda_aiv.model.AlertResponse
import com.skyautonet.seda_aiv.model.AlertItem
import com.skyautonet.seda_aiv.data.Result

object FakeSARemoteDataSource : SADataSource {

    private val ALERT_DEFAULT_DATA = AlertResponse().apply {
        add(AlertItem("LEFT_OFF"))
        add(AlertItem("RIGHT_OFF"))
        add(AlertItem("TOP_OFF"))
        add(AlertItem("BOTTOM_OFF"))
    }

    private val observableAlerts = MutableLiveData<Result<AlertResponse>>()

    override fun observeAlerts(): LiveData<Result<AlertResponse>> {
        return observableAlerts
    }

    override suspend fun getAlerts(): Result<AlertResponse> {
        return observableAlerts.value ?: Result.Success(ALERT_DEFAULT_DATA)
    }

    override suspend fun refreshAlerts() {
    }

}