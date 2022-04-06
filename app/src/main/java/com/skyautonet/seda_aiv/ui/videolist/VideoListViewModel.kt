package com.skyautonet.seda_aiv.ui.videolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.data.Result
import com.skyautonet.seda_aiv.data.Result.Success
import com.skyautonet.seda_aiv.data.source.SARepository
import com.skyautonet.seda_aiv.model.VideoItem
import com.skyautonet.seda_aiv.model.VideoListResponse
import kotlinx.coroutines.launch

class VideolistViewModel(
    private val saRepository: SARepository = SAApp.saRepository,
) : ViewModel() {
    enum class VideoType(private val typeValue: Int) {
        TOTAL(0), DRIVING(1), PARKING(2), EVENT(3);

        fun getValue(): Int {
            return typeValue
        }
    }

    val videoList: MutableList<VideoItem>
        get() =
            if (_videoListResponse.value is Success) {
                (_videoListResponse.value as Success<VideoListResponse>).data
            } else {
                mutableListOf()
            }

    private var _videoListResponse = saRepository.observeVideoList()
    val videoListResponse: LiveData<Result<VideoListResponse>>
        get() = _videoListResponse

    private var videoType: VideoType = VideoType.TOTAL


    fun setVideoType(videoType: VideoType) {
        this.videoType = videoType
    }

    fun getVideoList() {
        viewModelScope.launch {
            saRepository.refreshVideoList(videoType.getValue())
        }
    }

}