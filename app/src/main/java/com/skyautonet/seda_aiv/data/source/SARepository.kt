package com.skyautonet.seda_aiv.data.source

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.skyautonet.seda_aiv.common.CommonUtils
import com.skyautonet.seda_aiv.common.SharedPref
import com.skyautonet.seda_aiv.model.AlertResponse
import com.skyautonet.seda_aiv.rest.SAAppInterface
import com.skyautonet.seda_aiv.data.Result
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

    abstract fun observeAlerts(): LiveData<Result<AlertResponse>>
    abstract suspend fun getAlerts(): Result<AlertResponse>
    abstract suspend fun refreshAlerts()
}