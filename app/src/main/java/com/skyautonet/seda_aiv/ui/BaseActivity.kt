package com.skyautonet.seda_aiv.ui

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.common.CommonUtils
import com.skyautonet.seda_aiv.common.SharedPref
import com.skyautonet.seda_aiv.rest.SAAppInterface

import retrofit2.Retrofit
import javax.inject.Inject

open class BaseActivity : AppCompatActivity(), BaseUseCase {

    lateinit var dialog: Dialog

    @Inject
    lateinit var saAppInterface: SAAppInterface

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var commonUtils: CommonUtils

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as SAApp).getNetComponent().inject(this)
        dialog = commonUtils.createCustomLoader(this, false)
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()

    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0)
    }

    override fun showLoader() {
        dialog.show()
    }

    override fun hideLoader() {
        dialog.dismiss()
    }

    override fun onBackPress() {
    }


}