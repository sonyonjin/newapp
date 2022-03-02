package com.skyautonet.seda_aiv

import android.app.Application
import android.text.TextUtils
import android.util.Log
import com.skyautonet.seda_aiv.dagger.component.DaggerNetComponent
import com.skyautonet.seda_aiv.dagger.component.NetComponent
import com.skyautonet.seda_aiv.dagger.module.APIModule
import com.skyautonet.seda_aiv.dagger.module.AppModule
import com.skyautonet.seda_aiv.dagger.module.NetModule
import com.skyautonet.seda_aiv.rest.ApiClient
import com.skyautonet.seda_aiv.util.RoomDatabaseUtil
import com.skyautonet.seda_aiv.util.SdCardUtil
import java.io.File

class SAApp : Application() {

    companion object {
        lateinit var instance: SAApp
    }

    private lateinit var netComponent: NetComponent

    fun getNetComponent(): NetComponent {
        return netComponent
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        init()
    }

    private fun init() {
        netComponent = DaggerNetComponent.builder()
            .appModule(AppModule(this))
            .netModule(NetModule(ApiClient.BASE_URL_APP))
            .aPIModule(APIModule())
            .build()
    }
}