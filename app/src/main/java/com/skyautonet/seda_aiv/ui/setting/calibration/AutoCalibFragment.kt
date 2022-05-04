package com.skyautonet.seda_aiv.ui.setting.calibration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.databinding.FragmentAutoCalibBinding
import com.skyautonet.seda_aiv.ui.BaseFragment
import com.skyautonet.seda_aiv.ui.MainActivity

class AutoCalibFragment : BaseFragment() {
    private var _binding: FragmentAutoCalibBinding? = null
    private lateinit var viewModel: AutoCalibViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(AutoCalibViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_auto_calib, container, false)
        _binding = FragmentAutoCalibBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }

        startAutoCalib()
        return view
    }

    private fun startAutoCalib() {
        showLoader()

        viewModel.startLoading() {
            hideLoader()
            if (activity is MainActivity) {
                val mainActivity = activity as MainActivity
                mainActivity.loadFullScreen(PinPointSetFragment())
            }
        }
    }
}