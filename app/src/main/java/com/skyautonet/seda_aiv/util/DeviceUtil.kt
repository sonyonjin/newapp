package com.skyautonet.seda_aiv.util

import android.text.TextUtils
import android.os.Environment
import android.annotation.SuppressLint
import android.content.ContentResolver
import com.skyautonet.seda_aiv.util.DeviceUtil
import android.os.Build
import android.os.PowerManager.WakeLock
import android.os.PowerManager
import android.accounts.Account
import android.accounts.AccountManager
import android.app.KeyguardManager
import android.content.Context
import android.provider.Settings
import java.io.File
import java.io.IOException

object DeviceUtil {
    @JvmStatic
    fun isExternalStorageMounted(path: String?): Boolean {
        if (TextUtils.isEmpty(path)) {
            val state = Environment.getExternalStorageState()
            if (TextUtils.isEmpty(state)) {
                return false
            }
            if (state == Environment.MEDIA_MOUNTED) {
                return true
            }
        } else {
            val file = File(path)
            if (file != null) {
                try {
                    if (file.exists() && file.canRead() && file.canWrite()) {
                        return true
                    }
                } catch (e: SecurityException) {
                    val err = if (e.message == null) "Fail" else e.message!!
                }
            }
        }
        return false
    }

    @get:SuppressLint("NewApi")
    val isStorageRemovable: Boolean
        get() {
            var isRemovable = false
            try {
                isRemovable = Environment.isExternalStorageRemovable()
            } catch (e: NoSuchMethodError) {
                val err =
                    if (e.message == null) "Getting if external storage is removable failed" else e.message!!
            }
            return isRemovable
        }

    @JvmStatic
    fun checkExternalStoragePath(checkPath: String): Boolean {
        val file = Environment.getExternalStorageDirectory()
        return if (file != null && !TextUtils.isEmpty(file.path)
            && !TextUtils.isEmpty(checkPath)
        ) {
            checkPath == file.path
        } else false
    }

    fun getAndroidId(context: Context): String {
        val cr = context.contentResolver
        // UPD-START-{juliet} 2013/2/21 APIの廃止対応
        //return Settings.Secure.getString(cr, Settings.System.ANDROID_ID);
        return Settings.Secure.getString(cr, Settings.Secure.ANDROID_ID)
        // UPD-END-{juliet} 2013/2/21 APIの廃止対応
    }

    fun isInstallingUnknownAppsAllowed(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android O 以降
            context.packageManager.canRequestPackageInstalls()
        } else {
            Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.INSTALL_NON_MARKET_APPS, 0
            ) > 0
        }
    }

    fun acquireWakeLock(context: Context, tag: String?): WakeLock {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag)
        wakeLock.acquire()
        return wakeLock
    }

    fun releaseWakeLock(wakeLock: WakeLock?) {
        wakeLock?.release()
    }

    fun inKeyguardRestrictedInputMode(context: Context): Boolean {
        val keyManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return if (keyManager.inKeyguardRestrictedInputMode()) {
            true
        } else false
    }
}