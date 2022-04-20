package com.skyautonet.seda_aiv.util

import androidx.lifecycle.LiveData
import com.skyautonet.seda_aiv.data.AppConfig
import com.skyautonet.seda_aiv.data.source.local.db.AppDatabase
import com.skyautonet.seda_aiv.data.source.local.db.entity.AppManagedConfig

object RoomDatabaseUtil {

    fun updateAppConfig(appManagedConfig: AppManagedConfig) {
        val database = AppDatabase.getDatabase()
        val dao = database.appManagedConfigDao()

        appManagedConfig.id = 1 // Forcibly change Primary Key to 1(Always overwrite Insert)
        dao.insertAppManagedConfig(appManagedConfig)
    }

    fun getApplicationConfigData(): AppConfig {
        val database = AppDatabase.getDatabase()
        val dao = database.appManagedConfigDao()
        val appManagedConfig = dao.appManagedConfigData.firstOrNull() ?: AppManagedConfig()
        return AppConfig(appManagedConfig)
    }

    fun getApplicationConfigLiveData(): LiveData<List<AppManagedConfig>> {
        val database = AppDatabase.getDatabase()
        val dao = database.appManagedConfigDao()
        return dao.appManagedConfigLiveData
    }

}