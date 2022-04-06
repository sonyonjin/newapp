package com.skyautonet.seda_aiv.ui.videolist

import com.skyautonet.seda_aiv.model.VideoItem

interface VideoItemClick {
    fun onDownload(item: VideoItem)
}