package com.skyautonet.seda_aiv.ui.setting.calibration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.data.ResultObj
import com.skyautonet.seda_aiv.databinding.FragmentCalibRegiBinding
import com.skyautonet.seda_aiv.ui.BaseFragment

class CalibRegiFragment : BaseFragment() {
    private var _binding: FragmentCalibRegiBinding? = null
    private lateinit var viewModel: CalibRegiViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(CalibRegiViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_calib_regi, container, false)
        _binding = FragmentCalibRegiBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }

        viewModel.explan.observe(viewLifecycleOwner) {
            binding.tvCalibRegiExplan.text = it
        }
        viewModel.calibRegiResponse.observe(viewLifecycleOwner) {
            if (it is ResultObj.Success) {
                viewModel.setIsCalibRegisted(it.data.is_calib_registed)
            }
            hideLoader()
        }

        initView()

        showLoader()
        viewModel.getCalibRegi()
        return view
    }

    private fun initView() {
        binding.tvCalibRegiBtnSetup.setOnClickListener {
            onBtnSelected(CameraChannelSelectFragment())
        }
    }

    private fun onBtnSelected(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fl_container, fragment)
            ?.commit()
    }
}