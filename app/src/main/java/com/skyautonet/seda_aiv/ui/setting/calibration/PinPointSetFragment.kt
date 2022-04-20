package com.skyautonet.seda_aiv.ui.setting.calibration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.databinding.FragmentPinPointSetBinding
import com.skyautonet.seda_aiv.ui.BaseFragment

class PinPointSetFragment : BaseFragment() {
    private var _binding: FragmentPinPointSetBinding? = null
    private lateinit var viewModel: PinPointSetViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(PinPointSetViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_pin_point_set, container, false)
        _binding = FragmentPinPointSetBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }

        return view
    }
}