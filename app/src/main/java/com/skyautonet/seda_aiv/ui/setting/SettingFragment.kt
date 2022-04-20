package com.skyautonet.seda_aiv.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.databinding.FragmentSettingBinding
import com.skyautonet.seda_aiv.ui.BaseFragment
import com.skyautonet.seda_aiv.ui.setting.calibration.CalibRegiFragment

class SettingFragment : BaseFragment() {

    private var _binding: FragmentSettingBinding? = null
    private lateinit var viewModel: SettingViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        _binding = FragmentSettingBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }
        initView()
        return view
    }

    private fun initView() {
        binding.llSettingMenuDvrOption.setOnClickListener {
            performClick(it)
        }
        binding.llSettingMenuCalibration.setOnClickListener {
            performClick(it)
        }
        binding.llSettingMenuNetwork.setOnClickListener {
            performClick(it)
        }
        binding.llSettingMenuDisplay.setOnClickListener {
            performClick(it)
        }
        binding.llSettingMenuCameraBrightness.setOnClickListener {
            performClick(it)
        }
        binding.llSettingMenuEngineerMode.setOnClickListener {
            performClick(it)
        }
        setSelectedBtn(binding.llSettingMenuDvrOption)
    }

    private fun performClick(view: View) {
        setSelectedBtn(view)
    }

    private fun setSelectedBtn(view: View) {
        setSelectedBtnAll(false)
        view.isSelected = true

        when (view) {
            binding.llSettingMenuDvrOption -> {
            }
            binding.llSettingMenuCalibration -> {
                onMenuSelected(CalibRegiFragment())
            }
            binding.llSettingMenuNetwork -> {
            }
            binding.llSettingMenuDisplay -> {
            }
            binding.llSettingMenuCameraBrightness -> {
            }
            binding.llSettingMenuEngineerMode -> {
            }
        }
    }

    private fun onMenuSelected(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fl_container, fragment)
            ?.commit()
    }

    private fun setSelectedBtnAll(isSelected: Boolean) {
        _binding?.llSettingMenuDvrOption?.isSelected = isSelected
        _binding?.llSettingMenuCalibration?.isSelected = isSelected
        _binding?.llSettingMenuNetwork?.isSelected = isSelected
        _binding?.llSettingMenuDisplay?.isSelected = isSelected
        _binding?.llSettingMenuCameraBrightness?.isSelected = isSelected
        _binding?.llSettingMenuEngineerMode?.isSelected = isSelected
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}