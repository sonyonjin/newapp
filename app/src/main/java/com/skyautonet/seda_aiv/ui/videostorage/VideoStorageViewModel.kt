package com.skyautonet.seda_aiv.ui.videostorage

import androidx.lifecycle.viewModelScope
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.data.source.SARepository
import com.skyautonet.seda_aiv.data.source.local.file.VideoFile
import com.skyautonet.seda_aiv.ui.BaseViewModel
import kotlinx.coroutines.launch
import java.util.*

class VideoStorageViewModel(private val saRepository: SARepository = SAApp.saRepository,
) : BaseViewModel() {

    var _videoList = saRepository.observeVideoStorage()
    val videoList: MutableList<VideoFile>
        get() =
            if (_videoList.value != null && _videoList.value!!.size > 0) {
                makeVideoFileList(_videoList.value!!)
            } else {
                mutableListOf()
            }

    private var videoType = VideoFile.VideoType.TOTAL

    private fun makeVideoFileList(videoList: MutableList<VideoFile>): MutableList<VideoFile> {
        val result = ArrayList<VideoFile>()

        for (videoFile in videoList) {
            if (videoFile.videoType.isIncludeType(videoType)) {
                result.add(videoFile)
            }
        }
        return result
    }

    fun deleteVideoFile(videoList: MutableList<VideoFile>) {
        viewModelScope.launch {
            saRepository.deleteVideoFile(videoList)
        }
    }

    fun setVideoType(videoType: VideoFile.VideoType) {
        this.videoType = videoType
    }

    fun getTotalCount(): Int {
        return videoList.size
    }

    fun getVideoStorage() {
        viewModelScope.launch {
            // getVideoStorage는 수집되어있는 목록을 가져오므로 항상 최신의것을 원하면 새로목록을 생성해서 받아오도록한다.
            saRepository.refreshVideoStorage()
        }
    }
}