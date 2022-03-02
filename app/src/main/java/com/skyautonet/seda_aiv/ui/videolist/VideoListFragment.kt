package com.skyautonet.seda_aiv.ui.videolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.skyautonet.seda_aiv.databinding.FragmentVideoListBinding
import com.skyautonet.seda_aiv.ui.BaseFragment

class VideoListFragment : BaseFragment() {

    private var _binding: FragmentVideoListBinding? = null
    private lateinit var viewModel: VideolistViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(VideolistViewModel::class.java)

        _binding = FragmentVideoListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textVideoList
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}