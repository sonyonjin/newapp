package com.skyautonet.seda_aiv.ui.liveview

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.data.AppConfig
import com.skyautonet.seda_aiv.data.source.SARepository
import com.skyautonet.seda_aiv.data.source.local.db.entity.AppManagedConfig
import com.skyautonet.seda_aiv.model.AlertResponse
import com.skyautonet.seda_aiv.util.RoomDatabaseUtil
import com.skyautonet.seda_aiv.data.ResultObj
import com.skyautonet.seda_aiv.data.ResultObj.Success
import com.skyautonet.seda_aiv.data.succeeded
import com.skyautonet.seda_aiv.model.CalibrationListResponse
import com.skyautonet.seda_aiv.model.SetViewModeResponse
import com.skyautonet.seda_aiv.ui.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LiveViewViewModel(
    private val saRepository: SARepository = SAApp.saRepository
) : BaseViewModel() {

    enum class ViewMode(private val apiValue: Int) {
        TWO_D(0), THREE_D_REAR(1), THREE_D_REAR_RIGHT(2), THREE_D_REAR_LEFT(3), THREE_D_FRONT(4);

        fun getApiValue(): Int {
            return apiValue
        }
    }

    private val _appConfig = MutableLiveData<AppConfig>()
    val appConfig: LiveData<AppConfig>
        get() = _appConfig

    private val _appManagedConfigListLiveData = RoomDatabaseUtil.getApplicationConfigLiveData()

    private val appManagedConfigListObserver by lazy {
        Observer<List<AppManagedConfig>>() { managedConfigList ->
            if (managedConfigList.isNotEmpty()) {
                val managedConfig: AppManagedConfig = managedConfigList[0]
                updateManagedConfig(managedConfig)
            }
        }
    }

    private var _alertResponse = saRepository.observeAlerts()
    private val _alertResponseObserver by lazy {
        Observer<ResultObj<AlertResponse>>() { result ->
            if (result.succeeded) {
                updateAlert(result)
            }
        }
    }

    var isVisibleAlertLeft = MutableLiveData(ObservableBoolean(false))
    var isVisibleAlertRight = MutableLiveData(ObservableBoolean(false))
    var isVisibleAlertTop = MutableLiveData(ObservableBoolean(false))
    var isVisibleAlertBottom = MutableLiveData(ObservableBoolean(false))

    init {
        _appConfig.value = RoomDatabaseUtil.getApplicationConfigData()
        _appManagedConfigListLiveData.observeForever(appManagedConfigListObserver)
        _alertResponse.observeForever(_alertResponseObserver)
    }

    override fun onCleared() {
        super.onCleared()
        _appManagedConfigListLiveData.removeObserver(appManagedConfigListObserver)
        _alertResponse.removeObserver(_alertResponseObserver)
    }

    private fun computeResult(alertResponseResultObj: ResultObj<AlertResponse>, checkString: String): Boolean {
        return if (alertResponseResultObj is Success) {
                val alertResponse = alertResponseResultObj.data
                for (i in alertResponse) {
                    if (checkString.equals(i.alert_mode)) {
                        return true
                    }
                }
            false
        } else false
    }


    fun updateManagedConfig(managedConfig: AppManagedConfig?) {
        _appConfig.value = if (managedConfig != null) {
            AppConfig(managedConfig)
        } else {
            RoomDatabaseUtil.getApplicationConfigData()
        }
    }

    fun startAlert() {
        viewModelScope.launch {
            while(true) {
                delay(1000)
                saRepository.refreshAlerts()
            }
        }
    }

    fun updateAlert(resultObj: ResultObj<AlertResponse>) {
            isVisibleAlertLeft.value = ObservableBoolean(computeResult(resultObj, "LEFT=ON"))
            isVisibleAlertRight.value = ObservableBoolean(computeResult(resultObj, "RIGHT=ON"))
            isVisibleAlertTop.value = ObservableBoolean(computeResult(resultObj, "TOP=ON"))
            isVisibleAlertBottom.value = ObservableBoolean(computeResult(resultObj, "BOTTOM=ON"))
    }

    var SetViewModeResponse: SetViewModeResponse? = null

    fun setViewModel(viewModel: ViewMode) {
        viewModelScope.launch {
            if (commonUtils.isNetworkAvailable) {
                val videoListCall: Call<SetViewModeResponse> = saAppInterface.set_view_mode(viewModel.getApiValue())
                var resultObj: ResultObj<SetViewModeResponse>

                videoListCall.enqueue(object: Callback<SetViewModeResponse> {
                    override fun onResponse(
                        call: Call<SetViewModeResponse>,
                        response: Response<SetViewModeResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            SetViewModeResponse = response.body()!!
                            resultObj = ResultObj.Success(response.body()!!)
                        } else {
                            resultObj = ResultObj.Error(Exception("ApiCall failed"))
                        }
                    }

                    override fun onFailure(call: Call<SetViewModeResponse>, t: Throwable) {
                        resultObj = ResultObj.Error(Exception(t.message))
                    }
                })
            }
        }
    }

}