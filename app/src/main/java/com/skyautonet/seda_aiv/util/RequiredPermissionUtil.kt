package com.skyautonet.seda_aiv.util

import android.Manifest
import android.content.Context
import androidx.core.content.PermissionChecker


class RequiredPermissionUtil {

    companion object {

        const val REQUEST_ID_SA_PERMISSIONS = 10001

        val SA_PERMISSIONS: Array<String> = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        fun isCheckPermissions(context: Context, permissions: Array<String>): Boolean {
            var result = true
            for (permission in permissions) {
                if (PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_DENIED) {
                    result = false
                    break
                }
            }

            return result
        }
    }
}