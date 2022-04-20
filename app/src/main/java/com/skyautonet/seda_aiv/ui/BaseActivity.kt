package com.skyautonet.seda_aiv.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.common.CommonUtils
import com.skyautonet.seda_aiv.common.SharedPref
import com.skyautonet.seda_aiv.ui.splash.SplashActivity
import javax.inject.Inject

open class BaseActivity : AppCompatActivity(), IBaseActivity, BaseUseCase {

    lateinit var dialog: Dialog

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var commonUtils: CommonUtils
    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 최초 Activity인 SplashActivity에서는 permission획득후 하도록 해야하므로 제외함
        if (this !is SplashActivity) {
            (application as SAApp).getNetComponent().inject(this)
            dialog = commonUtils.createCustomLoader(this, false)
        }
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

    override fun onResumedFragment(fragment: BaseFragment) {
    }
}