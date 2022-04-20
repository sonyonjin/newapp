package com.skyautonet.seda_aiv.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.data.AppConfig
import com.skyautonet.seda_aiv.data.source.local.db.entity.AppManagedConfig
import com.skyautonet.seda_aiv.ui.BaseViewModel
import com.skyautonet.seda_aiv.util.RoomDatabaseUtil
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : BaseViewModel() {

    private val _appConfig = MutableLiveData<AppConfig>()
    val appConfig: LiveData<AppConfig>
        get() = _appConfig

    private val _connectDate = MutableLiveData<String>().apply {
        val date = Date(System.currentTimeMillis())
        val outputFormat: java.text.DateFormat = SimpleDateFormat("yyyy.MM.dd")
        value = outputFormat.format(date)
    }
    val connectDate: LiveData<String> = _connectDate

    private val _appManagedConfigListLiveData = RoomDatabaseUtil.getApplicationConfigLiveData()

    private val appManagedConfigListObserver by lazy {
        Observer<List<AppManagedConfig>>() { managedConfigList ->
            if (managedConfigList.isNotEmpty()) {
                val managedConfig: AppManagedConfig = managedConfigList[0]
                updateManagedConfig(managedConfig)
            }
        }
    }

    init {
        _appConfig.value = RoomDatabaseUtil.getApplicationConfigData()
        _appManagedConfigListLiveData.observeForever(appManagedConfigListObserver)
    }

    private val _userName = MutableLiveData<String>().apply {
        value = appConfig.value?.ssid
    }
    val userName: LiveData<String> = _userName

    private val _productRegNum = MutableLiveData<String>().apply {
        value = appConfig.value?.regNo
    }
    val productRegNum: LiveData<String> = _productRegNum

    private val _networkState = MutableLiveData<String>().apply {
        value = SAApp.instance.getString(R.string.home_network_state_no_connection)
    }
    val networkState: LiveData<String> = _networkState

    override fun onCleared() {
        super.onCleared()
        _appManagedConfigListLiveData.removeObserver(appManagedConfigListObserver)
    }

    fun updateManagedConfig(managedConfig: AppManagedConfig?) {
        _appConfig.value = if (managedConfig != null) {
            AppConfig(managedConfig)
        } else {
            RoomDatabaseUtil.getApplicationConfigData()
        }
    }

    fun confirmNetworkState() {
        var isConnected = false

        appConfig.value?.ssid?.let {
            isConnected = commonUtils.confirmSSID(SAApp.instance, it)
        }

        if (isConnected) {
            _networkState.value = SAApp.instance.getString(R.string.home_network_state_connected)
        } else {
            _networkState.value = SAApp.instance.getString(R.string.home_network_state_no_connection)
        }
    }
}