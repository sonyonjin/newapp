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
import com.skyautonet.seda_aiv.ui.setting.calibration.*

class SettingFragment : BaseFragment() {

    companion object {
        const val SETTING_MENU_SELECT = "setting_menu_select"
        const val TARGET_SCREEN = "target_screen"
    }

    enum class SETTING_MENU(private val position: Int) {
        DVR_OPTION(0), CALIBRATION(1), NETWORK(2), DISPLAY(3), CAMERA_BRIGHTNESS(4), ENGINEER_MODE(5);

        fun getPosition(): Int {
            return position
        }
    }

    enum class CALIBRATION_TARGET_SCREEN(private val position: Int) {
        CALIB_REGI(0), CAMERA_CHANNEL_SELECT(1), CAMERA_CHANNEL(2), AUTO_CALIB(3), PIN_POINT_SET(4), CAR_SIZE_INPUT(5), CALIB_COMPLETE(6);

        fun getPosition(): Int {
            return position
        }
    }

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
            setSelectedBtn(it, null)
        }
        binding.llSettingMenuCalibration.setOnClickListener {
            setSelectedBtn(it, CalibRegiFragment())
        }
        binding.llSettingMenuNetwork.setOnClickListener {
            setSelectedBtn(it, null)
        }
        binding.llSettingMenuDisplay.setOnClickListener {
            setSelectedBtn(it, null)
        }
        binding.llSettingMenuCameraBrightness.setOnClickListener {
            setSelectedBtn(it, null)
        }
        binding.llSettingMenuEngineerMode.setOnClickListener {
            setSelectedBtn(it, null)
        }

        val settingMenuSelect = arguments?.getInt(SETTING_MENU_SELECT)
        when (settingMenuSelect) {
            SETTING_MENU.DVR_OPTION.getPosition() -> {
                setSelectedBtn(binding.llSettingMenuDvrOption, null)
            }
            SETTING_MENU.CALIBRATION.getPosition() -> {
                val targetScreen = arguments?.getInt(TARGET_SCREEN)
                val targetFragment: Fragment = when (targetScreen) {
                    CALIBRATION_TARGET_SCREEN.CALIB_REGI.getPosition() -> CalibRegiFragment()
                    CALIBRATION_TARGET_SCREEN.CAMERA_CHANNEL_SELECT.getPosition() -> CameraChannelSelectFragment()
                    CALIBRATION_TARGET_SCREEN.CAMERA_CHANNEL.getPosition() -> CameraChannelFragment()
                    CALIBRATION_TARGET_SCREEN.AUTO_CALIB.getPosition() -> AutoCalibFragment()
                    CALIBRATION_TARGET_SCREEN.PIN_POINT_SET.getPosition() -> PinPointSetFragment()
                    CALIBRATION_TARGET_SCREEN.CAR_SIZE_INPUT.getPosition() -> CarSizeInputFragment()
                    CALIBRATION_TARGET_SCREEN.CALIB_COMPLETE.getPosition() -> CalibCompleteFragment()
                    else -> {CalibRegiFragment()}
                }
                setSelectedBtn(binding.llSettingMenuCalibration, targetFragment)
            }
            SETTING_MENU.NETWORK.getPosition() -> {
                setSelectedBtn(binding.llSettingMenuNetwork, null)
            }
            SETTING_MENU.DISPLAY.getPosition() -> {
                setSelectedBtn(binding.llSettingMenuDisplay, null)
            }
            SETTING_MENU.CAMERA_BRIGHTNESS.getPosition() -> {
                setSelectedBtn(binding.llSettingMenuCameraBrightness, null)
            }
            SETTING_MENU.ENGINEER_MODE.getPosition() -> {
                setSelectedBtn(binding.llSettingMenuEngineerMode, null)
            }
            else -> {
                setSelectedBtn(binding.llSettingMenuDvrOption, null)
            }
        }
    }

    private fun setSelectedBtn(view: View, fragment: Fragment?) {
        setSelectedBtnAll(false)
        view.isSelected = true

        fragment?.let {
            when (view) {
                binding.llSettingMenuDvrOption -> {
                }
                binding.llSettingMenuCalibration -> {
                    onMenuSelected(it)
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