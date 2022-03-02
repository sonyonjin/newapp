package com.skyautonet.seda_aiv.util

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import com.skyautonet.seda_aiv.storage.entity.AppManagedConfig
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

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
            val str = loadConfigFile(it)
            if (str == null) {
                saveConfigFile(it, "Enter data without any space after '=' and end the line with a ';' also don't change the arrangement\nssid=;\npassword=;\nProduct Reg.No=;\nRTSP Link (Camera)=;\nRTSP Link (Channel1)=;\nRTSP Link (Channel2)=;\nIP Address of Access Point=;\n")
            } else {
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
                }

                return if (errorMsg.length > 0) {
                    errorMsg.toString()
                } else {
                    RoomDatabaseUtil.updateAppConfig(appManagedConfig)
                    null
                }
            }
        }
        return null
    }
}