package com.skyautonet.seda_aiv.ui.videostorage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import com.skyautonet.seda_aiv.databinding.FragmentVideoStorageBinding
import com.skyautonet.seda_aiv.ui.BaseFragment

class VideoStorageFragment : BaseFragment() {

    private var _binding: FragmentVideoStorageBinding? = null
    private lateinit var viewModel: VideoStorageViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(VideoStorageViewModel::class.java)

        _binding = FragmentVideoStorageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.wvVideo.loadUrl("http://172.16.30.218:5000/")
        binding.wvVideo.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}