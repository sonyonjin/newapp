package com.skyautonet.seda_aiv.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.skyautonet.seda_aiv.model.AlertResponse
import com.skyautonet.seda_aiv.data.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DefaultRemoteSARepository(
    private val saRemoteDataSource: SADataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
) : SARepository() {
    override fun observeAlerts(): LiveData<Result<AlertResponse>> {
        return saRemoteDataSource.observeAlerts()
    }

    override suspend fun getAlerts(): Result<AlertResponse> {
        return saRemoteDataSource.getAlerts()
    }

    override suspend fun refreshAlerts() {
        updateAlertFromRemoteDataSource()
    }

    private fun getMutableObserveAlerts(): MutableLiveData<Result<AlertResponse>>? {
        return if (observeAlerts() is MutableLiveData) observeAlerts() as MutableLiveData<Result<AlertResponse>> else null
    }

    private fun updateAlertFromRemoteDataSource() {
        getMutableObserveAlerts()?.let {
            if (commonUtils.isNetworkAvailable) {
                val alertCall: Call<AlertResponse> = saAppInterface.alert()
                var result: Result<AlertResponse>

                alertCall.enqueue(object: Callback<AlertResponse> {
                    override fun onResponse(
                        call: Call<AlertResponse>,
                        response: Response<AlertResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            result = Result.Success(response.body()!!)
                        } else {
                            result = Result.Error(Exception("AlertCall failed"))
                        }
                        it.postValue(result)
                    }

                    override fun onFailure(call: Call<AlertResponse>, t: Throwable) {
                        result = Result.Error(Exception(t.message))
                        it.postValue(result)
                    }
                })
            }
        }
    }


}