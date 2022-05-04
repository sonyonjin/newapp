package com.skyautonet.seda_aiv.ui.setting.calibration

import androidx.lifecycle.viewModelScope
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.data.source.SARepository
import com.skyautonet.seda_aiv.ui.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AutoCalibViewModel (
    private val saRepository: SARepository = SAApp.saRepository,
) : BaseViewModel() {

    fun startLoading(runnable: Runnable) {

        viewModelScope.launch {
            delay(3000)
            runnable.run()
        }
    }

}