package com.skyautonet.seda_aiv.data.source.local.file

import java.io.File
import java.util.*

open class VideoFile(
    var videoType: VideoType = VideoType.TOTAL,
    var file: File,
    var fileName: String,
    var size: Long,
    var startTime: Date?
) {

    var isDeleteSelected: Boolean = false

    enum class VideoType(private val videoType: Int, private val apiValue: Int) {
        TOTAL(VIDEO_TYPE_TOTAL, 0),
        DRIVING(VIDEO_TYPE_DRIVING, 1),
        PARKING(VIDEO_TYPE_PARKING, 2),
        EVENT(VIDEO_TYPE_EVENT, 3);

        fun getVideoType(): Int {
            return videoType
        }

        fun getApiValue(): Int {
            return apiValue
        }

        fun isIncludeType(videoType: VideoType): Boolean {
            return this.videoType and videoType.getVideoType() != 0
        }
    }

    companion object {
        const val VIDEO_TYPE_DRIVING = 1
        const val VIDEO_TYPE_PARKING = 1 shl 1
        const val VIDEO_TYPE_EVENT = 1 shl 2
        const val VIDEO_TYPE_TOTAL = VIDEO_TYPE_DRIVING or VIDEO_TYPE_PARKING or VIDEO_TYPE_EVENT
    }

    fun getStartTimeMillis(): Long? {
        return startTime?.time
    }
}