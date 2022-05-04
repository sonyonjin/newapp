package com.skyautonet.seda_aiv.data.source

import VideoFileManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.data.ResultObj
import com.skyautonet.seda_aiv.data.source.local.file.VideoFile
import com.skyautonet.seda_aiv.model.*
import com.skyautonet.seda_aiv.util.SdCardUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
/*
 * BluePrint예제처럼 repository를 구성해서 해보았으나 Remote, Local을 유동적으로 동시에 저장할 필요도 딱히 없고하여
 * 일일이 interface추가적으로 정의해야되는 작업이 군더더기 작업이라 향후 API call은 ViewModel안에서 직접 콜하도록 변경
 *
 * 만약 Remote, Local을 동시에 유기적으로 저장해야되는 경우만 사용하도록함
 */

class DefaultSARepository(
    private val saRemoteDataSource: SARemoteDataSource,
    private val saLocalDataSource: SALocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
) : SARepository() {
    private val videoFileManager = VideoFileManager()

    override fun observeAlerts(): LiveData<ResultObj<AlertResponse>> {
        return saRemoteDataSource.observeAlerts()
    }

    override fun getAlerts(): ResultObj<AlertResponse> {
        return saRemoteDataSource.getAlerts()
    }

    override suspend fun refreshAlerts() {
        updateAlertFromRemoteDataSource()
    }

    private fun getMutableObserveAlerts(): MutableLiveData<ResultObj<AlertResponse>>? {
        return if (observeAlerts() is MutableLiveData) observeAlerts() as MutableLiveData<ResultObj<AlertResponse>> else null
    }

    private fun updateAlertFromRemoteDataSource() {
        getMutableObserveAlerts()?.let {
            if (commonUtils.isNetworkAvailable) {
                val alertCall: Call<AlertResponse> = saAppInterface.alert()
                var resultObj: ResultObj<AlertResponse>

                alertCall.enqueue(object: Callback<AlertResponse> {
                    override fun onResponse(
                        call: Call<AlertResponse>,
                        response: Response<AlertResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            resultObj = ResultObj.Success(response.body()!!)
                        } else {
                            resultObj = ResultObj.Error(Exception("AlertCall failed"))
                        }
                        it.postValue(resultObj)
                    }

                    override fun onFailure(call: Call<AlertResponse>, t: Throwable) {
                        resultObj = ResultObj.Error(Exception(t.message))
                        it.postValue(resultObj)
                    }
                })
            }
        }
    }

    override fun observeVideoList(): LiveData<ResultObj<VideoListResponse>> {
        return saRemoteDataSource.observeVideoList()
    }

    override fun getVideoList(): ResultObj<VideoListResponse> {
        return saRemoteDataSource.getVideoList()
    }

    override suspend fun refreshVideoList(videoType: Int) {
        updateVideoListFromRemoteDataSource(videoType)
    }

    private fun getMutableObserveVideoList(): MutableLiveData<ResultObj<VideoListResponse>>? {
        return if (observeVideoList() is MutableLiveData) observeVideoList() as MutableLiveData<ResultObj<VideoListResponse>> else null
    }

    private fun updateVideoListFromRemoteDataSource(videoType: Int) {
        getMutableObserveVideoList()?.let {
            if (commonUtils.isNetworkAvailable) {
                val videoListCall: Call<VideoListResponse> = saAppInterface.video_list(videoType)
                var resultObj: ResultObj<VideoListResponse>

                videoListCall.enqueue(object: Callback<VideoListResponse> {
                    override fun onResponse(
                        call: Call<VideoListResponse>,
                        response: Response<VideoListResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            resultObj = ResultObj.Success(response.body()!!)
                        } else {
                            resultObj = ResultObj.Error(Exception("ApiCall failed"))
                        }
                        it.postValue(resultObj)
                    }

                    override fun onFailure(call: Call<VideoListResponse>, t: Throwable) {
                        resultObj = ResultObj.Error(Exception(t.message))
                        it.postValue(resultObj)
                    }
                })
            }
        }
    }

    override fun observeVideoStorage(): LiveData<MutableList<VideoFile>> {
        return saLocalDataSource.observeVideoStorage()
    }

    override fun getVideoStorage(): MutableList<VideoFile> {
        return saLocalDataSource.getVideoStorage()
    }

    override suspend fun refreshVideoStorage() {
        updateVideoStorageFromLocalDataSource()
    }

    override suspend fun deleteVideoFile(videoList: MutableList<VideoFile>) {
        deleteVideoStorageFromLocalDataSource(videoList)
    }

    private fun getMutableObserveVideoStorage(): MutableLiveData<MutableList<VideoFile>>? {
        return if (observeVideoStorage() is MutableLiveData) observeVideoStorage() as MutableLiveData<MutableList<VideoFile>> else null
    }

    private fun updateVideoStorageFromLocalDataSource() {
        getMutableObserveVideoStorage()?.let {
            videoFileManager.clearVideoFile()

            val videoDirectory = SdCardUtil.getVideoFileDirectory(SAApp.instance)
            if (videoDirectory != null) {
                val files = videoDirectory.listFiles()
                if (files != null) {
                    for (file in files) {
                        videoFileManager.inputVideoFile(file)
                    }
                }
            }
            val videoFileList = videoFileManager.getVideoFileList()
            it.postValue(videoFileList)
        }
    }

    private fun deleteVideoStorageFromLocalDataSource(videoList: MutableList<VideoFile>) {
        for (i in videoList.size - 1 downTo 0) {
            val videoFile = videoList.get(i)
            if (videoFile.file.delete()) {
                videoList.remove(videoFile)
            }
        }
        updateVideoStorageFromLocalDataSource()
    }

    override fun observeCalibRegi(): LiveData<ResultObj<CalibRegiResponse>> {
        return saRemoteDataSource.observeCalibRegi()
    }

    override fun getCalibRegi(): ResultObj<CalibRegiResponse> {
        return saRemoteDataSource.getCalibRegi()
    }

    override suspend fun refreshCalibRegi() {
        updateCalibRegiFromRemoteDataSource()
    }

    private fun getMutableObserveCalibRegi(): MutableLiveData<ResultObj<CalibRegiResponse>>? {
        return if (observeCalibRegi() is MutableLiveData) observeCalibRegi() as MutableLiveData<ResultObj<CalibRegiResponse>> else null
    }

    private fun updateCalibRegiFromRemoteDataSource() {
        getMutableObserveCalibRegi()?.let {
            if (commonUtils.isNetworkAvailable) {
                val calibRegi: Call<CalibRegiResponse> = saAppInterface.calib_regi()
                var resultObj: ResultObj<CalibRegiResponse>

                calibRegi.enqueue(object: Callback<CalibRegiResponse> {
                    override fun onResponse(
                        call: Call<CalibRegiResponse>,
                        response: Response<CalibRegiResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            resultObj = ResultObj.Success(response.body()!!)
                        } else {
                            resultObj = ResultObj.Error(Exception("ApiCall failed"))
                        }
                        it.postValue(resultObj)
                    }

                    override fun onFailure(call: Call<CalibRegiResponse>, t: Throwable) {
                        resultObj = ResultObj.Error(Exception(t.message))
                        it.postValue(resultObj)
                    }
                })
            }
        }
    }

    override fun observeSetCameraChannel(): LiveData<ResultObj<SetCameraChannelResponse>> {
        return saRemoteDataSource.observeSetCameraChannel()
    }

    override suspend fun setCameraChannel(channelMode: SARemoteDataSource.ChannelMode) {
        setCameraChannelFromRemoteDataSource(channelMode.getApiValue())
    }

    private fun getMutableObserveSetCameraChannel(): MutableLiveData<ResultObj<SetCameraChannelResponse>>? {
        return if (observeSetCameraChannel() is MutableLiveData) observeSetCameraChannel() as MutableLiveData<ResultObj<SetCameraChannelResponse>> else null
    }

    private fun setCameraChannelFromRemoteDataSource(channel_mode: Int) {
        getMutableObserveSetCameraChannel()?.let {
            if (commonUtils.isNetworkAvailable) {
                val calibRegi: Call<SetCameraChannelResponse> = saAppInterface.set_camera_channel(channel_mode)
                var resultObj: ResultObj<SetCameraChannelResponse>

                calibRegi.enqueue(object: Callback<SetCameraChannelResponse> {
                    override fun onResponse(
                        call: Call<SetCameraChannelResponse>,
                        response: Response<SetCameraChannelResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            resultObj = ResultObj.Success(response.body()!!)
                        } else {
                            resultObj = ResultObj.Error(Exception("ApiCall failed"))
                        }
                        it.postValue(resultObj)
                    }

                    override fun onFailure(call: Call<SetCameraChannelResponse>, t: Throwable) {
                        resultObj = ResultObj.Error(Exception(t.message))
                        it.postValue(resultObj)
                    }
                })
            }
        }
    }

    override fun observeGetCameraChannel(): LiveData<ResultObj<GetCameraChannelResponse>> {
        return saRemoteDataSource.observeGetCameraChannel()
    }

    override fun getCameraChannel(): ResultObj<GetCameraChannelResponse> {
        return saRemoteDataSource.getCameraChannel()
    }

    override suspend fun refreshCameraChannel() {
        getCameraChannelFromRemoteDataSource()
    }

    private fun getMutableObserveGetCameraChannel(): MutableLiveData<ResultObj<GetCameraChannelResponse>>? {
        return if (observeGetCameraChannel() is MutableLiveData) observeGetCameraChannel() as MutableLiveData<ResultObj<GetCameraChannelResponse>> else null
    }

    private fun getCameraChannelFromRemoteDataSource() {
        getMutableObserveGetCameraChannel()?.let {
            if (commonUtils.isNetworkAvailable) {
                val calibRegi: Call<GetCameraChannelResponse> = saAppInterface.get_camera_channel()
                var resultObj: ResultObj<GetCameraChannelResponse>

                calibRegi.enqueue(object: Callback<GetCameraChannelResponse> {
                    override fun onResponse(
                        call: Call<GetCameraChannelResponse>,
                        response: Response<GetCameraChannelResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            resultObj = ResultObj.Success(response.body()!!)
                        } else {
                            resultObj = ResultObj.Error(Exception("ApiCall failed"))
                        }
                        it.postValue(resultObj)
                    }

                    override fun onFailure(call: Call<GetCameraChannelResponse>, t: Throwable) {
                        resultObj = ResultObj.Error(Exception(t.message))
                        it.postValue(resultObj)
                    }
                })
            }
        }
    }

}