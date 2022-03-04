package com.skyautonet.seda_aiv.ui.liveview

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.data.AppConfig
import com.skyautonet.seda_aiv.data.source.SARepository
import com.skyautonet.seda_aiv.data.source.local.entity.AppManagedConfig
import com.skyautonet.seda_aiv.model.AlertResponse
import com.skyautonet.seda_aiv.util.RoomDatabaseUtil
import com.skyautonet.seda_aiv.data.Result
import com.skyautonet.seda_aiv.data.Result.Success
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LiveViewViewModel(
    private val alertSARepository: SARepository = SAApp.saRepository
) : ViewModel() {

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

    private var _alertResponse = alertSARepository.observeAlerts()
    private val _alertResponseObserver by lazy {
        Observer<Result<AlertResponse>>() { result ->
            updateAlert(result)
        }
    }

    init {
        _appConfig.value = RoomDatabaseUtil.getApplicationConfigData()
        _appManagedConfigListLiveData.observeForever(appManagedConfigListObserver)
        _alertResponse.observeForever(_alertResponseObserver)
    }

    var isVisibleAlertLeft = MutableLiveData(ObservableBoolean(false))
    var isVisibleAlertRight = MutableLiveData(ObservableBoolean(false))
    var isVisibleAlertTop = MutableLiveData(ObservableBoolean(false))
    var isVisibleAlertBottom = MutableLiveData(ObservableBoolean(false))

    private fun computeResult(alertResponseResult: Result<AlertResponse>, checkString: String): Boolean {
        return if (alertResponseResult is Success) {
                val alertResponse = alertResponseResult.data
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
                alertSARepository.refreshAlerts()
            }
        }
    }

    fun updateAlert(result: Result<AlertResponse>) {
            isVisibleAlertLeft.value = ObservableBoolean(computeResult(result, "LEFT=ON"))
            isVisibleAlertRight.value = ObservableBoolean(computeResult(result, "RIGHT=ON"))
            isVisibleAlertTop.value = ObservableBoolean(computeResult(result, "TOP=ON"))
            isVisibleAlertBottom.value = ObservableBoolean(computeResult(result, "BOTTOM=ON"))
    }

}