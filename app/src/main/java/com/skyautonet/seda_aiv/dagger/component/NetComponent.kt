package com.skyautonet.seda_aiv.dagger.component


import com.skyautonet.seda_aiv.dagger.module.APIModule
import com.skyautonet.seda_aiv.dagger.module.AppModule
import com.skyautonet.seda_aiv.dagger.module.NetModule
import com.skyautonet.seda_aiv.data.source.SARepository
import com.skyautonet.seda_aiv.ui.BaseFragment
import com.skyautonet.seda_aiv.ui.BaseActivity
import com.skyautonet.seda_aiv.ui.BaseViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetModule::class, APIModule::class, AppModule::class])
interface NetComponent {
    fun inject(saRepository: SARepository)
    fun inject(baseActivity: BaseActivity)
    fun inject(baseFragment: BaseFragment)
    fun inject(baseViewModel: BaseViewModel)
}