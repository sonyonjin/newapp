package com.skyautonet.seda_aiv.model

class VideoListResponse : ArrayList<VideoItem>()

class VideoItem (
    var id: Int = -1,
    var file_name: String = "",
    var date_time: String = "",
    var size: String = ""
) {
    @Transient
    var isEnable: Boolean = true
}