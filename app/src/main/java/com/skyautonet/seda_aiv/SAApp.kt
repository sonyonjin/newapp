package com.skyautonet.seda_aiv

import android.app.Application
import com.skyautonet.seda_aiv.dagger.component.DaggerNetComponent
import com.skyautonet.seda_aiv.dagger.component.NetComponent
import com.skyautonet.seda_aiv.dagger.module.APIModule
import com.skyautonet.seda_aiv.dagger.module.AppModule
import com.skyautonet.seda_aiv.dagger.module.NetModule
import com.skyautonet.seda_aiv.data.source.*

class SAApp : Application() {

    companion object {
        lateinit var instance: SAApp
        lateinit var saRepository: SARepository
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
            .netModule(NetModule())
            .aPIModule(APIModule())
            .build()
    }
}