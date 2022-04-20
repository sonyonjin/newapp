package com.skyautonet.seda_aiv.ui

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.common.CommonUtils
import com.skyautonet.seda_aiv.common.SharedPref
import com.skyautonet.seda_aiv.rest.SAAppInterface
import com.skyautonet.seda_aiv.ui.splash.SplashViewModel
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var commonUtils: CommonUtils
    @Inject
    lateinit var sharePref: SharedPref
    @Inject
    lateinit var saAppInterface: SAAppInterface

    init {
        // 최초 ViewModel인 SplashViewModel에서는 permission획득후 하도록 해야하므로 제외함
        if (this !is SplashViewModel) {
            val injector = SAApp.instance.getNetComponent()
            injector.inject(this)
        }
    }
}
