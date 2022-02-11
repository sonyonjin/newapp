package com.skyautonet.seda_aiv

import android.app.Application
import com.skyautonet.seda_aiv.dagger.component.DaggerNetComponent
import com.skyautonet.seda_aiv.dagger.component.NetComponent
import com.skyautonet.seda_aiv.dagger.module.APIModule
import com.skyautonet.seda_aiv.dagger.module.AppModule
import com.skyautonet.seda_aiv.dagger.module.NetModule
import com.skyautonet.seda_aiv.rest.ApiClient

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
        init()
        instance = this
    }

    private fun init() {
        netComponent = DaggerNetComponent.builder()
            .appModule(AppModule(this))
            .netModule(NetModule(ApiClient.BASE_URL_APP))
            .aPIModule(APIModule())
            .build()
    }
}