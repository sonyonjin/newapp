package com.skyautonet.seda_aiv.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.skyautonet.seda_aiv.R

import java.text.ParseException
import java.text.SimpleDateFormat

class CommonUtils(internal var mContext: Context) {

    //check internet connection
    /* getting systems Service connectivity manager */// connected to the internet
    // connected to wifi
    // connected to the mobile provider's data plan
    // not connected to the internet
    val isNetworkAvailable: Boolean
        @SuppressLint("MissingPermission")
        get() {
            val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            if (activeNetwork != null) {
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                    return true
                } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                    return true
                }
            } else {
                return false
            }
            return false
        }

    val deviceID: String
        @SuppressLint("HardwareIds")
        get() = Settings.Secure.getString(
            mContext.contentResolver,
            Settings.Secure.ANDROID_ID
        )

    fun changeDateFormatFromAnother(date: String?): String? {
        @SuppressLint("SimpleDateFormat") val inputFormat: java.text.DateFormat =
            SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        @SuppressLint("SimpleDateFormat") val outputFormat: java.text.DateFormat =
            SimpleDateFormat("MMM dd, HH:mm")
        var resultDate = ""
        try {
            resultDate = outputFormat.format(inputFormat.parse(date))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return resultDate
    }


    fun changeDateFormatFromAnother1(date: String?): String? {
        @SuppressLint("SimpleDateFormat") val inputFormat: java.text.DateFormat =
            SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        @SuppressLint("SimpleDateFormat") val outputFormat: java.text.DateFormat =
            SimpleDateFormat("MM dd, yyyy a, HH:mm")
        var resultDate = ""
        try {
            resultDate = outputFormat.format(inputFormat.parse(date))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return resultDate
    }

    fun changeChartDateFormatFromAnother(date: String?): String? {
        @SuppressLint("SimpleDateFormat") val inputFormat: java.text.DateFormat =
            SimpleDateFormat("yyyy-MM-dd")
        @SuppressLint("SimpleDateFormat") val outputFormat: java.text.DateFormat =
            SimpleDateFormat("EEE\ndd")
        var resultDate = ""
        try {
            resultDate = outputFormat.format(inputFormat.parse(date))
        } catch (e: ParseException) {
            e.printStackTrace()
        }finally {
            return resultDate
        }
    }

    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses =
                am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo =
                am.getRunningTasks(1)
            val componentInfo = taskInfo[0].topActivity
            if (componentInfo!!.packageName == context.packageName) {
                isInBackground = false
            }
        }
        return isInBackground
    }

    fun toastShow(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }

    fun toastShowLong(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show()
    }

    // soft keyboard hide
    fun hideKeyboard(activity: Activity) {
        // Check if no view has focus:
        try {
            val view = activity.currentFocus
            if (view != null) {
                val inputManager =
                    mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(
                    view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        } catch (e: Exception) {

        }
    }

    // soft keyboard hide
    fun hideKeyboard(activity: Activity, view: View?) {
        // Check if no view has focus:
        try {
            if (view != null) {
                val inputManager =
                    mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } catch (e: Exception) {

        }
    }

    // soft keyboard show
    fun showKeyboard(activity: Activity, view: View?) {
        // Check if no view has focus:
        try {
            if (view != null) {
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        } catch (e: Exception) {

        }
    }

    fun createCustomLoader(mContext: Context, isCancelable: Boolean): Dialog {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setDimAmount(0f)
        dialog.setCancelable(isCancelable)
        dialog.setContentView(R.layout.loader_progress_dialog)


        //Grab the window of the dialog, and change the width
        val lp = WindowManager.LayoutParams()
        val window = dialog.window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = lp
        return dialog
    }

    fun showCustomDialog(dialog: Dialog?, context: Context) {
        if (dialog != null) {
            if (!dialog.isShowing)
                if (!(context as Activity).isFinishing) {
                    dialog.show()
                }
        }
    }

    fun confirmSSID(context: Context, ssid: String): Boolean {
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager;
        val networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (networkInfo?.isConnected == true) {
            val wifiManager = context.getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
            val connectionInfo = wifiManager.connectionInfo

            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.ssid)) {
                if (connectionInfo.ssid.replace("\"", "").equals(ssid)) {
                    return true
                }
            }
        }
        return false
    }
}

