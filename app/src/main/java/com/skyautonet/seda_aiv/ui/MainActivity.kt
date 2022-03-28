package com.skyautonet.seda_aiv.ui

import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.fragment.app.Fragment
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.databinding.ActivityMainBinding
import com.skyautonet.seda_aiv.ui.home.HomeFragment
import com.skyautonet.seda_aiv.ui.liveview.LiveViewFragment
import com.skyautonet.seda_aiv.ui.setting.SettingFragment
import com.skyautonet.seda_aiv.ui.videolist.VideoListFragment
import com.skyautonet.seda_aiv.ui.videostorage.VideoStorageFragment

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.visibility = View.GONE

        initView()
    }

    private fun initView() {
        setSelectedMenu(binding.clMenuHome)
        onMenuSelected(HomeFragment())

        binding.clMenuHome.setOnClickListener {
            performClick(it, HomeFragment())
        }
        binding.clMenuLive.setOnClickListener {
            performClick(it, LiveViewFragment())
        }
        binding.clMenuVideoList.setOnClickListener {
            performClick(it, VideoListFragment())
        }
        binding.clMenuVideoStorage.setOnClickListener {
            performClick(it, VideoStorageFragment())
        }
        binding.clMenuSetting.setOnClickListener {
            performClick(it, SettingFragment())
        }

        binding.btnSlide.setOnClickListener {
            setSlideMenu(isExpanded())
        }
        binding.btnMenuSlide.setOnClickListener {
            setSlideMenu(isExpanded())
        }
    }

    override fun onResumedFragment(fragment: BaseFragment) {
        super.onResumedFragment(fragment)
        setEnabledMenuAll(true)
        if (fragment is LiveViewFragment) {
            if (!isExpanded()) {
                setSlideMenu(false)
            }
        }
    }

    private fun isExpanded(): Boolean {
        return binding.ivExpandMenu.visibility == View.VISIBLE
    }

    private fun performClick(view: View, fragment: Fragment) {
        setSelectedMenu(view)
        onMenuSelected(fragment)
    }

    private fun onMenuSelected(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.cl_container, fragment)
            .commit()
        setEnabledMenuAll(false)
    }

    private fun setSlideMenu(isExpand: Boolean) {
        val animation = if (isExpand) AlphaAnimation(0F, 1F) else AlphaAnimation(1F, 0F)
        animation.duration = 500
        animation.fillAfter = true
        binding.navView.startAnimation(animation)

        setEnabledMenuAll(isExpand)

        val animate = TranslateAnimation(
            if (isExpand) -binding.navView.width.toFloat() else 0F,
            if (isExpand) 0F else -binding.navView.width.toFloat(),
            0F,
            0F
        )

        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                if (isExpand) {
                    binding.ivCollapseMenu.visibility = View.VISIBLE
                    binding.ivExpandMenu.visibility = View.INVISIBLE
                    binding.btnMenuSlide.visibility = View.GONE
                } else {
                    binding.ivCollapseMenu.visibility = View.INVISIBLE
                    binding.ivExpandMenu.visibility = View.VISIBLE
                    binding.btnMenuSlide.visibility = View.VISIBLE
                }
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
        animate.duration = 500
        animate.fillAfter = true
        binding.clMenu.startAnimation(animate)
    }

    private fun setSelectedMenu(view: View) {
        setSelectedMenuAll(false)
        view.isSelected = true
    }

    private fun setSelectedMenuAll(isSelected: Boolean) {
        binding.clMenuHome.isSelected = isSelected
        binding.clMenuLive.isSelected = isSelected
        binding.clMenuVideoList.isSelected = isSelected
        binding.clMenuVideoStorage.isSelected = isSelected
        binding.clMenuSetting.isSelected = isSelected
    }

    private fun setEnabledMenuAll(isEnabled: Boolean) {
        binding.clMenuHome.isEnabled = isEnabled
        binding.clMenuLive.isEnabled = isEnabled
        binding.clMenuVideoList.isEnabled = isEnabled
        binding.clMenuVideoStorage.isEnabled = isEnabled
        binding.clMenuSetting.isEnabled = isEnabled
    }
}