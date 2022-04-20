package com.skyautonet.seda_aiv.data.source

import androidx.lifecycle.LiveData
import com.skyautonet.seda_aiv.data.source.local.file.VideoFile

interface SALocalDataSource {
    fun observeVideoStorage(): LiveData<MutableList<VideoFile>>
    fun getVideoStorage(): MutableList<VideoFile>
    suspend fun refreshVideoStorage(videoType: Int)
}