package com.skyautonet.seda_aiv.ui.liveview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.skyautonet.seda_aiv.databinding.FragmentLiveViewBinding
import com.skyautonet.seda_aiv.ui.BaseFragment

class LiveViewFragment : BaseFragment() {

    private var _binding: FragmentLiveViewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(LiveViewViewModel::class.java)

        _binding = FragmentLiveViewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textLiveView
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}