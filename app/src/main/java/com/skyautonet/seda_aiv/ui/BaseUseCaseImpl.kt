package com.skyautonet.seda_aiv.ui

import com.google.gson.Gson
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.common.CommonUtils
import com.skyautonet.seda_aiv.common.SharedPref
import javax.inject.Inject

abstract class BaseUseCaseImpl(var mBaseView: BaseView) : BaseUseCase {

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var commonUtils: CommonUtils
    @Inject
    lateinit var sharePref: SharedPref


    init {
        val injector = SAApp.instance.getNetComponent()
        injector.inject(this)
    }

    override  fun showLoader() {
        mBaseView.showLoader()
    }

    override  fun hideLoader() {
        mBaseView.hideLoader()
    }

    override  fun onBackPress() {
        mBaseView.onBackPress()
    }
}
