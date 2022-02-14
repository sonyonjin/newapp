package com.skyautonet.seda_aiv.ui.videostorage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.skyautonet.seda_aiv.databinding.FragmentVideoStorageBinding
import com.skyautonet.seda_aiv.ui.BaseFragment

class VideoStorageFragment : BaseFragment() {

    private var _binding: FragmentVideoStorageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(VideoStorageViewModel::class.java)

        _binding = FragmentVideoStorageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textVideoStorage
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}