package com.skyautonet.seda_aiv.util

import java.io.*
import java.lang.Exception
import kotlin.Throws

object FileUtil {

    @Throws(IOException::class)
    fun copyFile(inputStream: InputStream?, destFile: File?): Boolean {
        if (inputStream == null || destFile == null) {
            return false
        }
        try {
            FileOutputStream(destFile).use { out ->
                val buffer = ByteArray(4096)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } >= 0) {
                    out.write(buffer, 0, bytesRead)
                }
                return true
            }
        } catch (e: IOException) {
            throw e
        }
    }

    @Throws(IOException::class)
    fun copyFile(`in`: File?, out: File?) {
        FileInputStream(`in`).channel.use { inChannel ->
            FileOutputStream(out).channel.use { outChannel ->
                inChannel.transferTo(
                    0,
                    inChannel.size(),
                    outChannel
                )
            }
        }
    }
    fun toByteArray(file: File): ByteArray? {
        try {
            FileInputStream(file).use { fis ->
                val data = ByteArray(file.length().toInt())
                fis.read(data)
                return data
            }
        } catch (e: Exception) {
            return null
        }
    }
}