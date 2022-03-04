package com.skyautonet.seda_aiv.data.source

import androidx.lifecycle.LiveData
import com.skyautonet.seda_aiv.model.AlertResponse
import com.skyautonet.seda_aiv.data.Result

interface SADataSource {
    fun observeAlerts(): LiveData<Result<AlertResponse>>
    suspend fun getAlerts(): Result<AlertResponse>
    suspend fun refreshAlerts()
}