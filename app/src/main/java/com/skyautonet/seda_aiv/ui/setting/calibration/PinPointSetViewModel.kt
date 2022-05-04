package com.skyautonet.seda_aiv.ui.setting.calibration

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.data.ResultObj
import com.skyautonet.seda_aiv.data.source.SARepository
import com.skyautonet.seda_aiv.model.CalibrationItem
import com.skyautonet.seda_aiv.model.CalibrationListResponse
import com.skyautonet.seda_aiv.ui.BaseViewModel
import com.skyautonet.seda_aiv.util.ImageUtil
import com.skyautonet.seda_aiv.util.SdCardUtil
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PinPointSetViewModel (
    private val saRepository: SARepository = SAApp.saRepository,
) : BaseViewModel() {

    var observeCalibrationList = MutableLiveData<ResultObj<CalibrationListResponse>>()
    var calibrationListResponse: CalibrationListResponse? = null

    var currentStep = 0
    var maxStep = 3
    var photoImage = MutableLiveData<Bitmap>()

    fun getCalibrationList() {
        viewModelScope.launch {
            if (commonUtils.isNetworkAvailable) {
                val videoListCall: Call<CalibrationListResponse> = saAppInterface.cabration_list()
                var resultObj: ResultObj<CalibrationListResponse>

                videoListCall.enqueue(object: Callback<CalibrationListResponse> {
                    override fun onResponse(
                        call: Call<CalibrationListResponse>,
                        response: Response<CalibrationListResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            calibrationListResponse = response.body()!!
                            resultObj = ResultObj.Success(response.body()!!)
                        } else {
                            resultObj = ResultObj.Error(Exception("ApiCall failed"))
                        }
                        observeCalibrationList.postValue(resultObj)
                    }

                    override fun onFailure(call: Call<CalibrationListResponse>, t: Throwable) {
                        resultObj = ResultObj.Error(Exception(t.message))
                        observeCalibrationList.postValue(resultObj)
                    }
                })
            }
        }
    }

    fun downloadCalibration(calibrationItem: CalibrationItem, listener: ImageDownloadListener) {
        viewModelScope.launch {
            if (commonUtils.isNetworkAvailable) {
                val downloadVideoCall: Call<ResponseBody> = saAppInterface.download_calibration(calibrationItem.file_name)
                downloadVideoCall.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>,
                    ) {
                        if (response.body() != null) {
                            SdCardUtil.getCalibrationFile(SAApp.instance, calibrationItem.file_name)?.let {
                                SdCardUtil.saveFile(it, response.body()!!.byteStream())
                                calibrationItem.isDownloaded = true
                                calibrationItem.file = it
                                listener.onDownloadCompleted(true)
                                return
                            }
                            listener.onDownloadCompleted(false)
                        } else {
                            listener.onDownloadCompleted(false)
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        listener.onDownloadCompleted(false)
                    }
                })
            }
        }
    }

    fun convertImage(item: CalibrationItem, reqWidth: Int, reqHeight: Int) {
        if (item.id == currentStep) {
            viewModelScope.launch {
                ImageUtil.getAlignImage(item.file?.path, reqWidth, reqHeight)?.let {
                    photoImage.value = it
                }
            }
        }
    }

    interface ImageDownloadListener {
        fun onDownloadCompleted(isSuccess: Boolean)
    }
}