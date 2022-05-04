package com.skyautonet.seda_aiv.ui.setting.calibration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.databinding.FragmentCalibCompleteBinding
import com.skyautonet.seda_aiv.ui.BaseFragment
import com.skyautonet.seda_aiv.ui.MainActivity
import com.skyautonet.seda_aiv.ui.setting.SettingFragment

class CalibCompleteFragment : BaseFragment() {
    private var _binding: FragmentCalibCompleteBinding? = null
    private lateinit var viewModel: CalibCompleteViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(CalibCompleteViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_calib_complete, container, false)
        _binding = FragmentCalibCompleteBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }

        initView()
        return view
    }

    private fun initView() {
        binding.tvCalibCompleteBtnPreview.setOnClickListener {
            onPrevBtnSelected()
        }
        binding.tvCalibCompleteBtnFinish.setOnClickListener {
            onNextBtnSelected()
        }
    }

    private fun onPrevBtnSelected() {
        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity
            mainActivity.loadFullScreen(CarSizeInputFragment())
        }
    }

    private fun onNextBtnSelected() {
        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity
            val fragment = SettingFragment()
            val bundle = Bundle()
            bundle.putInt(SettingFragment.SETTING_MENU_SELECT, SettingFragment.SETTING_MENU.CALIBRATION.getPosition())
            bundle.putInt(SettingFragment.TARGET_SCREEN, SettingFragment.CALIBRATION_TARGET_SCREEN.CALIB_REGI.getPosition())
            fragment.arguments = bundle
            mainActivity.releaseFullScreen(fragment)
        }
    }
}