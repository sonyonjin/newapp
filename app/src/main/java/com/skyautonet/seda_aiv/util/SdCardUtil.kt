package com.skyautonet.seda_aiv.util

import android.content.Context
import android.os.Environment
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.data.source.local.db.entity.AppManagedConfig
import java.io.*

object SdCardUtil {

    private fun getMountedExternalMediaFileDir(context: Context): String? {
        val fileArrays = context.getExternalFilesDirs(null)
        return if (fileArrays != null && fileArrays.size > 0) {
            val getPath = fileArrays[0].path
            getPath.replace("/data", "/media")
        } else {
            null
        }
    }

    private fun getConfigDirectory(context: Context): File? {
        return when (Environment.getExternalStorageState()) {
            Environment.MEDIA_MOUNTED -> {
                val pathString = getMountedExternalMediaFileDir(context)
                return pathString?.let {
                    File(pathString)
                }
            }
            else -> {
                null
            }
        }
    }

    private fun getConfigFile(context: Context): File? {
        val configDirectory = getConfigDirectory(context)
        if (configDirectory?.exists() == false) {
            configDirectory.mkdirs()
        }
        return configDirectory?.toString()?.let {
            File(it, "config.txt")
        }
    }

    fun loadConfigFile(file: File): String? {
        if (file.exists()) {
            FileUtil.toByteArray(file)?.let {
                return String(it)
            }
        }
        return null
    }

    fun saveConfigFile(file: File, outputString: String) {
        try {
            FileOutputStream(file).use { output ->
                val byteArray = outputString.toByteArray()
                output.write(byteArray)
                output.flush()
            }
        } catch (e: IOException) {
            throw e
        }
    }

    fun setConfigFile(context: Context): String? {
        val errorMsg = StringBuffer()
        val appManagedConfig = AppManagedConfig()

        getConfigFile(context)?.let {
            var str = loadConfigFile(it)
            if (str == null) {
                str = context.resources.getString(R.string.config)
                saveConfigFile(it, str)
            }

            val dataConfig: List<String> = str.split(";")
            for (i in 1..7) {
                val keyAndValue = dataConfig[i].split("=")
                var check: Boolean = false
                when (i) {
                    1 -> {
                        keyAndValue[1].also { value ->
                            if (value.isEmpty()) {
                                check = true
                                errorMsg.append("SSID,")
                            } else {
                                appManagedConfig.ssid = value
                            }
                        }
                    }
                    2 -> {
                        keyAndValue[1].also { value ->
                            if (value.isEmpty()) {
                                check = true
                                errorMsg.append("Password,")
                            } else {
                                appManagedConfig.password = value
                            }
                        }
                    }
                    3 -> {
                        keyAndValue[1].also { value ->
                            if (value.isEmpty()) {
                                check = true
                                errorMsg.append("Reg no,,")
                            } else {
                                appManagedConfig.regNo = value
                            }
                        }
                    }
                    4 -> {
                        keyAndValue[1].also { value ->
                            if (value.isEmpty()) {
                                check = true
                                errorMsg.append("RTSP Link 1,")
                            } else {
                                appManagedConfig.rtspLink1 = value
                            }
                        }
                    }
                    5 -> {
                        keyAndValue[1].also { value ->
                            if (value.isEmpty()) {
                                check = true
                                errorMsg.append("RTSP Link 2,")
                            } else {
                                appManagedConfig.rtspLink2 = value
                            }
                        }
                    }
                    6 -> {
                        keyAndValue[1].also { value ->
                            if (value.isEmpty()) {
                                check = true
                                errorMsg.append("RTSP Link 3,")
                            } else {
                                appManagedConfig.rtspLink3 = value
                            }
                        }
                    }
                    7 -> {
                        keyAndValue[1].also { value ->
                            if (value.isEmpty()) {
                                check = true
                                errorMsg.append("IP Address,")
                            } else {
                                appManagedConfig.ipAddress = value
                            }
                        }
                    }
                }

                if (errorMsg.length > 0) {
                    return errorMsg.toString()
                }
            }
        }
        RoomDatabaseUtil.updateAppConfig(appManagedConfig)
        return null
    }

    fun getVideoFileDirectory(context: Context): File? {
        return when (Environment.getExternalStorageState()) {
            Environment.MEDIA_MOUNTED -> {
                val pathString = getMountedExternalMediaFileDir(context)
                return pathString?.let {
                    File(pathString + "/video_file")
                }
            }
            else -> {
                null
            }
        }
    }

    fun getVideoFile(context: Context, fileName: String): File? {
        val videoFileDirectory = getVideoFileDirectory(context)
        if (videoFileDirectory?.exists() == false) {
            videoFileDirectory.mkdirs()
        }
        return videoFileDirectory?.toString()?.let {
            File(it, fileName)
        }
    }

    fun saveFile(file: File, inputStream: InputStream) {
        try {
            FileOutputStream(file).use { output ->
                inputStream.copyTo(output)
                output.flush()
            }
        } catch (e: IOException) {
            throw e
        }
    }

    fun getCalibrationFileDirectory(context: Context): File? {
        return when (Environment.getExternalStorageState()) {
            Environment.MEDIA_MOUNTED -> {
                val pathString = getMountedExternalMediaFileDir(context)
                return pathString?.let {
                    File(pathString + "/calibration_file")
                }
            }
            else -> {
                null
            }
        }
    }

    fun getCalibrationFile(context: Context, fileName: String): File? {
        val calibrationFileDirectory = getCalibrationFileDirectory(context)
        if (calibrationFileDirectory?.exists() == false) {
            calibrationFileDirectory.mkdirs()
        }
        return calibrationFileDirectory?.toString()?.let {
            File(it, fileName)
        }
    }

}