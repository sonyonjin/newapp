package com.skyautonet.seda_aiv.ui.setting.calibration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.databinding.FragmentCarSizeInputBinding
import com.skyautonet.seda_aiv.ui.BaseFragment
import com.skyautonet.seda_aiv.ui.MainActivity

class CarSizeInputFragment : BaseFragment() {
    private var _binding: FragmentCarSizeInputBinding? = null
    private lateinit var viewModel: CarSizeInputViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(CarSizeInputViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_car_size_input, container, false)
        _binding = FragmentCarSizeInputBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }

        initView()
        return view
    }

    private fun initView() {
        binding.tvCarSizeInputBtnPreview.setOnClickListener {
            onPrevBtnSelected()
        }
        binding.tvCarSizeInputBtnNext.setOnClickListener {
            onNextBtnSelected()
        }
    }

    private fun onPrevBtnSelected() {
        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity
            mainActivity.loadFullScreen(PinPointSetFragment())
        }
    }

    private fun onNextBtnSelected() {
        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity
            mainActivity.loadFullScreen(CalibCompleteFragment())
        }
    }
}