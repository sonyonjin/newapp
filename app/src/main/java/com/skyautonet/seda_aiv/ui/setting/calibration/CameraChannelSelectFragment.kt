package com.skyautonet.seda_aiv.ui.setting.calibration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.data.ResultObj
import com.skyautonet.seda_aiv.data.source.SARemoteDataSource
import com.skyautonet.seda_aiv.databinding.FragmentCameraChannelSelectBinding
import com.skyautonet.seda_aiv.ui.BaseFragment

class CameraChannelSelectFragment : BaseFragment() {
    private var _binding: FragmentCameraChannelSelectBinding? = null
    private lateinit var viewModel: CameraChannelSelectViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(CameraChannelSelectViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_camera_channel_select, container, false)
        _binding = FragmentCameraChannelSelectBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }

        viewModel.getCameraChannelResponse.observe(viewLifecycleOwner) {
            if (it is ResultObj.Success) {
                when (it.data.channel_mode) {
                    SARemoteDataSource.ChannelMode.FOUR.getApiValue() -> {
                        setSelectedBtn(binding.ivCameraChannel4)
                    }
                    SARemoteDataSource.ChannelMode.SIX.getApiValue() -> {
                        setSelectedBtn(binding.ivCameraChannel6)
                    }
                    SARemoteDataSource.ChannelMode.EIGHT.getApiValue() -> {
                        setSelectedBtn(binding.ivCameraChannel8)
                    }
                }
            }
            hideLoader()
        }

        viewModel.setCameraChannelResponse.observe(viewLifecycleOwner) {
            hideLoader()
        }

        initView()

        showLoader()
        viewModel.getCameraChannel()
        return view
    }

    private fun initView() {
        binding.ivCameraChannel4.setOnClickListener {
            performClick(it)
        }
        binding.ivCameraChannel6.setOnClickListener {
            performClick(it)
        }
        binding.ivCameraChannel8.setOnClickListener {
            performClick(it)
        }

        binding.tvCameraChannelSelectBtnPreview.setOnClickListener {
            onBtnSelected(CalibRegiFragment())
        }
        binding.tvCameraChannelSelectBtnNext.setOnClickListener {
            onNextBtnSelected()
        }
    }

    private fun performClick(view: View) {
        setSelectedBtn(view)
    }

    private fun setSelectedBtn(view: View) {
        setSelectedBtnAll(false)
        view.isSelected = true

        when (view) {
            binding.ivCameraChannel4 -> {
                showLoader()
                viewModel.setChannelMode(SARemoteDataSource.ChannelMode.FOUR)
            }
            binding.ivCameraChannel6 -> {
                showLoader()
                viewModel.setChannelMode(SARemoteDataSource.ChannelMode.SIX)
            }
            binding.ivCameraChannel8 -> {
                showLoader()
                viewModel.setChannelMode(SARemoteDataSource.ChannelMode.EIGHT)
            }
        }
    }

    private fun setSelectedBtnAll(isSelected: Boolean) {
        _binding?.ivCameraChannel4?.isSelected = isSelected
        _binding?.ivCameraChannel6?.isSelected = isSelected
        _binding?.ivCameraChannel8?.isSelected = isSelected
    }

    private fun onBtnSelected(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fl_container, fragment)
            ?.commit()
    }

    private fun onNextBtnSelected() {
        val fragment = CameraChannelFragment()
        val bundle = Bundle()
        bundle.putInt(CameraChannelFragment.CAMERA_CHANNEL_MODE, viewModel.getChannelMode().getApiValue())
        fragment.arguments = bundle
        onBtnSelected(fragment)
    }
}