package com.skyautonet.seda_aiv.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.skyautonet.seda_aiv.data.source.local.file.VideoFile

object FakeSALocalDataSource : SALocalDataSource {

    private val observableVideoFile = ArrayList<VideoFile>()
    private val observableVideoStorage = MutableLiveData<MutableList<VideoFile>>()

    override fun observeVideoStorage(): LiveData<MutableList<VideoFile>> {
        return observableVideoStorage
    }

    override fun getVideoStorage(): MutableList<VideoFile> {
        return observableVideoStorage.value ?: ArrayList<VideoFile>()
    }

    override suspend fun refreshVideoStorage(videoType: Int) {
    }

}