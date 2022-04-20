package com.skyautonet.seda_aiv.data.source.local.file

import java.io.File
import java.util.*

class EventVideoFile(file: File,
                     fileName: String,
                     size: Long,
                     startDate: Date?
) : VideoFile(VideoType.EVENT, file, fileName, size, startDate)