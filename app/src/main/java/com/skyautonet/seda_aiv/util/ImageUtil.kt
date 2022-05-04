package com.skyautonet.seda_aiv.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

class ImageUtil {

    companion object {
        fun getAlignImage(path: String?, reqWidth: Int, reqHeight: Int): Bitmap? {
            if (path == null) return null
            if (!File(path).exists()) return null
            val bitmap: Bitmap? = resizeImage(path, reqWidth, reqHeight)
            return bitmap
        }

        fun resizeImage(path: String?, reqWidth: Int, reqHeight: Int): Bitmap? {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, options)

            options.inSampleSize = calculateInSampleSize(
                options,
                reqWidth,
                reqHeight)

            options.inJustDecodeBounds = false

            return BitmapFactory.decodeFile(path, options)
        }

        fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1
            if (height > reqHeight || width > reqWidth) {
                inSampleSize = if (width > height) {
                    width / reqWidth
                } else {
                    height / reqHeight
                }
            }
            return inSampleSize
        }
    }
}