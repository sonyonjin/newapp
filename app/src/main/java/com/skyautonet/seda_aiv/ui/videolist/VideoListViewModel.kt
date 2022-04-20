package com.skyautonet.seda_aiv.ui.videolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.data.ResultObj
import com.skyautonet.seda_aiv.data.ResultObj.Success
import com.skyautonet.seda_aiv.data.source.SARepository
import com.skyautonet.seda_aiv.data.source.local.file.VideoFile
import com.skyautonet.seda_aiv.model.VideoItem
import com.skyautonet.seda_aiv.model.VideoListResponse
import com.skyautonet.seda_aiv.ui.BaseViewModel
import com.skyautonet.seda_aiv.util.SdCardUtil
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideolistViewModel(
    private val saRepository: SARepository = SAApp.saRepository,
) : BaseViewModel() {

    private var videoType = VideoFile.VideoType.TOTAL

    val videoList: MutableList<VideoItem>
        get() =
            if (_videoListResponse.value is Success) {
                (_videoListResponse.value as Success<VideoListResponse>).data
            } else {
                mutableListOf()
            }

    private var _videoListResponse = saRepository.observeVideoList()
    val videoListResponse: LiveData<ResultObj<VideoListResponse>>
        get() = _videoListResponse

    val syncedDownloadedVideoFileObserve = MutableLiveData<MutableList<VideoItem>>()

    private var _videoStorage = saRepository.observeVideoStorage()
    private val _videoStorageObserver by lazy {
        Observer<MutableList<VideoFile>>() {
            makeDownloadedFileList(it)
        }
    }

    init {
        _videoStorage.observeForever(_videoStorageObserver)
    }

    override fun onCleared() {
        super.onCleared()
        _videoStorage.removeObserver(_videoStorageObserver)
    }

    fun setVideoType(videoType: VideoFile.VideoType) {
        this.videoType = videoType
    }

    fun getTotalCount(): Int {
        return videoList.size
    }

    fun getVideoList() {
        viewModelScope.launch {
            saRepository.refreshVideoList(videoType.getApiValue())
        }
    }

    fun syncDownloadedVideoFile() {
        viewModelScope.launch {
            saRepository.refreshVideoStorage()
        }
    }

    fun makeDownloadedFileList(videoStorage: MutableList<VideoFile>) {
        for (videoItem in videoList) {
            for (videoFile in videoStorage) {
                if (videoItem.file_name.equals(videoFile.fileName)) {
                    videoItem.isEnable = false
                    break
                }
            }
        }
        syncedDownloadedVideoFileObserve.value = videoList
    }

    fun downloadVideo(fileName: String, listener: VideoItemDownloadListener) {
        viewModelScope.launch {
            if (commonUtils.isNetworkAvailable) {
                val downloadVideoCall: Call<ResponseBody> = saAppInterface.download_video(fileName)
                downloadVideoCall.enqueue(object : Callback<ResponseBody>{
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>,
                    ) {
                        if (response.body() != null) {
                            SdCardUtil.getVideoFile(SAApp.instance, fileName)?.let {
                                SdCardUtil.saveVideoFile(it, response.body()!!.byteStream())
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

    interface VideoItemDownloadListener {
        fun onDownloadCompleted(isSuccess: Boolean)
    }

}