package com.skyautonet.seda_aiv.ui.setting.calibration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.data.ResultObj
import com.skyautonet.seda_aiv.data.source.SARepository
import com.skyautonet.seda_aiv.model.CalibRegiResponse
import com.skyautonet.seda_aiv.ui.BaseViewModel
import kotlinx.coroutines.launch

class CalibRegiViewModel (
    private val saRepository: SARepository = SAApp.saRepository,
) : BaseViewModel() {

    private var isCalibRegisted = false

    private val _explan = MutableLiveData<String>().apply {
        if (isCalibRegisted) {
            value = SAApp.instance.getString(R.string.calib_regi_explan_registed)
        } else {
            value = SAApp.instance.getString(R.string.calib_regi_explan)
        }
    }

    val explan: LiveData<String>
        get() = _explan

    private var _calibRegiResponse = saRepository.observeCalibRegi()
    val calibRegiResponse: LiveData<ResultObj<CalibRegiResponse>>
        get() = _calibRegiResponse

    fun getCalibRegi() {
        viewModelScope.launch {
            saRepository.refreshCalibRegi()
        }
    }

    fun setIsCalibRegisted(isCalibRegisted: Boolean) {
        this.isCalibRegisted = isCalibRegisted
    }
}