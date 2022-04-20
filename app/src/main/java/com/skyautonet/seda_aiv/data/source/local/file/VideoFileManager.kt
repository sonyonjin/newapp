import android.text.TextUtils
import com.skyautonet.seda_aiv.data.source.local.file.DrivingVideoFile
import com.skyautonet.seda_aiv.data.source.local.file.EventVideoFile
import com.skyautonet.seda_aiv.data.source.local.file.ParkingVideoFile
import com.skyautonet.seda_aiv.data.source.local.file.VideoFile
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class VideoFileManager {
    private val mVideoFileList: MutableList<VideoFile> = ArrayList<VideoFile>()

    fun inputVideoFile(inputVideoFile: VideoFile?) {
        inputVideoFile?.let {
            for (i in mVideoFileList.indices) {
                if (mVideoFileList[i].getStartTimeMillis() != null && it.getStartTimeMillis() != null) {
                    if (mVideoFileList[i].getStartTimeMillis()!! >= it.getStartTimeMillis()!!) { //시작시간 기준으로 오름차순정렬(최신의 맨위로)
                        //오름차순상 앞에 삽입
                        mVideoFileList.add(i, it)
                        return
                    }
                }
            }

            //정렬상 맨뒤에 추가
            mVideoFileList.add(it)
        }
    }

    private fun parsingStartTime(dateString: String): Date? {
        val sDateFormat = SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault())
        val startDateStr = dateString.substring(0, 14)
        return sDateFormat.parse(startDateStr)
    }

    fun inputVideoFile(file: File) {
        val parseStr = file.name.split("_")

        if (parseStr.size >= 2 && !TextUtils.isEmpty(parseStr[1]) && parseStr[1].length >= 20) {
            val videoFile = when (parseStr[0].lowercase(Locale.getDefault())) {
                "driving" -> DrivingVideoFile(file, file.name, file.length(), parsingStartTime(parseStr[1]))
                "parking" -> ParkingVideoFile(file, file.name, file.length(), parsingStartTime(parseStr[1]))
                "event" -> EventVideoFile(file, file.name, file.length(), parsingStartTime(parseStr[1]))
                else -> null
            }
            inputVideoFile(videoFile)
        }
    }

    fun clearVideoFile() {
        mVideoFileList.clear()
    }

    fun getVideoFileList(): MutableList<VideoFile> {
        return mVideoFileList
    }
}