package com.skyautonet.seda_aiv.model

class VideoListResponse : ArrayList<VideoItem>()

data class VideoItem (
    var id: String = "",
    var file_name: String = "",
    var date_time: String = "",
    var size: String = ""
)