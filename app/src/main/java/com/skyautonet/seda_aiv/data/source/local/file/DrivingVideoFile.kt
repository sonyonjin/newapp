package com.skyautonet.seda_aiv.data.source.local.file

import java.io.File
import java.util.*

class DrivingVideoFile(file: File,
                       fileName: String,
                       size: Long,
                       startDate: Date?
) : VideoFile(VideoType.DRIVING, file, fileName, size, startDate)