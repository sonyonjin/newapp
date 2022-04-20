package com.skyautonet.seda_aiv.ui.setting.calibration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.data.source.SARemoteDataSource
import com.skyautonet.seda_aiv.databinding.FragmentCameraChannelBinding
import com.skyautonet.seda_aiv.ui.BaseFragment

class CameraChannelFragment : BaseFragment() {

    companion object {
        const val CAMERA_CHANNEL_MODE = "camera_channel"
    }

    private var _binding: FragmentCameraChannelBinding? = null
    private lateinit var viewModel: CameraChannelViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(CameraChannelViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_camera_channel, container, false)
        _binding = FragmentCameraChannelBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }

        initView()

        return view
    }

    private fun initView() {
        arguments?.let {
            it.getInt(CAMERA_CHANNEL_MODE)?.let { cameraChannelMode ->
                when (cameraChannelMode) {
                    SARemoteDataSource.ChannelMode.FOUR.getApiValue() -> {
                        viewModel.setVisibleCameraChannel(SARemoteDataSource.ChannelMode.FOUR)
                    }
                    SARemoteDataSource.ChannelMode.SIX.getApiValue() -> {
                        viewModel.setVisibleCameraChannel(SARemoteDataSource.ChannelMode.SIX)
                    }
                    SARemoteDataSource.ChannelMode.EIGHT.getApiValue() -> {
                        viewModel.setVisibleCameraChannel(SARemoteDataSource.ChannelMode.EIGHT)
                    }
                }
            }
        }

        binding.tvCameraChannelSelectBtnPreview.setOnClickListener {
            onBtnSelected(CameraChannelSelectFragment())
        }
        binding.tvCameraChannelSelectBtnNext.setOnClickListener {
            onNextBtnSelected()
        }
    }

    private fun onBtnSelected(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fl_container, fragment)
            ?.commit()
    }

    private fun onNextBtnSelected() {
    }
}