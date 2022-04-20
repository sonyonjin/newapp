package com.skyautonet.seda_aiv.data.source.local.file

import java.io.File
import java.util.*

class ParkingVideoFile(file: File,
                       fileName: String,
                       size: Long,
                       startDate: Date?
) : VideoFile(VideoType.PARKING, file, fileName, size, startDate)