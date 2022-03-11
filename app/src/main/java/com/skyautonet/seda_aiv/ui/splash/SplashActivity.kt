package com.skyautonet.seda_aiv.ui.splash

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.skyautonet.seda_aiv.databinding.ActivitySplashBinding
import com.skyautonet.seda_aiv.ui.BaseActivity
import com.skyautonet.seda_aiv.ui.MainActivity
import com.skyautonet.seda_aiv.ui.home.HomeViewModel
import com.skyautonet.seda_aiv.util.RequiredPermissionUtil

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.progress.observe(this) {
            binding.progressBar.progress = it
        }
        viewModel.progressText.observe(this) {
            binding.tvProgress.text = it
        }

        if (!RequiredPermissionUtil.isCheckPermissions(this, RequiredPermissionUtil.SA_PERMISSIONS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(RequiredPermissionUtil.SA_PERMISSIONS, RequiredPermissionUtil.REQUEST_ID_SA_PERMISSIONS)
            } else {
                viewModel.setConfigFile()
            }
        } else {
            viewModel.setConfigFile()
            gotoMain()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val requirePermission: MutableList<String> = ArrayList()
            for (i in grantResults.indices) {
                val grantResult = grantResults[i]
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    requirePermission.add(permissions[i])
                }
            }

            if (requirePermission.size > 0) {
                requestPermissions(requirePermission.toTypedArray(), RequiredPermissionUtil.REQUEST_ID_SA_PERMISSIONS)
            } else {
                viewModel.setConfigFile()
                gotoMain()
            }
        }
    }

    fun gotoMain() {
        viewModel.startLoading() {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}