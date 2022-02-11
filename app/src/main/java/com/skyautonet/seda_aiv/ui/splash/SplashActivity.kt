package com.skyautonet.seda_aiv.ui.splash

import android.content.Intent
import android.os.Bundle
import com.skyautonet.seda_aiv.databinding.ActivitySplashBinding
import com.skyautonet.seda_aiv.ui.BaseActivity
import com.skyautonet.seda_aiv.ui.MainActivity

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoader()

        binding.root.postDelayed({
            hideLoader()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }, 3000)

    }
}