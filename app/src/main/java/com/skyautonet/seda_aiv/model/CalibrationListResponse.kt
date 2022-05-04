package com.skyautonet.seda_aiv.model

import java.io.File

class CalibrationListResponse : ArrayList<CalibrationItem>()

class CalibrationItem (
    var id: Int = -1,
    var file_name: String = "",
    var date_time: String = "",
    var size: String = ""
) {
    @Transient
    var isDownloaded: Boolean = false
    var file: File? = null
}