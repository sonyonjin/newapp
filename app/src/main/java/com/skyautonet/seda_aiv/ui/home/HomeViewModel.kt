package com.skyautonet.seda_aiv.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.skyautonet.seda_aiv.data.AppConfig
import com.skyautonet.seda_aiv.storage.entity.AppManagedConfig
import com.skyautonet.seda_aiv.util.RoomDatabaseUtil

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

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

    init {
        _appConfig.value = RoomDatabaseUtil.getApplicationConfigData()
        _appManagedConfigListLiveData.observeForever(appManagedConfigListObserver)
    }

    fun updateManagedConfig(managedConfig: AppManagedConfig?) {
        _appConfig.value = if (managedConfig != null) {
            AppConfig(managedConfig)
        } else {
            RoomDatabaseUtil.getApplicationConfigData()
        }
    }
}