package com.skyautonet.seda_aiv.ui.videolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.data.Result
import com.skyautonet.seda_aiv.databinding.FragmentVideoListBinding
import com.skyautonet.seda_aiv.model.VideoItem
import com.skyautonet.seda_aiv.ui.BaseFragment

class VideoListFragment : BaseFragment() {

    private var _binding: FragmentVideoListBinding? = null
    private lateinit var viewModel: VideolistViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var rvVideoList: RecyclerView
    private lateinit var srVideoList: SwipeRefreshLayout
    private lateinit var videoListAdapter: VideoListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this).get(VideolistViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_video_list, container, false)
        _binding = FragmentVideoListBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }
        initView()
        viewModel.videoListResponse.observe(viewLifecycleOwner) {
            if (it is Result.Success) {
                videoListAdapter.setList(viewModel.videoList)
                videoListAdapter.notifyDataSetChanged()
            }
        }
        viewModel.getVideoList()
        return view
    }

    private fun initView() {
        rvVideoList = binding.rvVideoList
        rvVideoList.layoutManager = LinearLayoutManager(activity)

        srVideoList = binding.srVideoList
        srVideoList.setOnRefreshListener {
            viewModel.getVideoList()
            srVideoList.isRefreshing = false
        }

        videoListAdapter = VideoListAdapter(requireContext(), viewModel.videoList, rvVideoList, object : VideoItemClick {
            override fun onDownload(item: VideoItem) {
                System.out.println("${item.file_name}")

            }
        })
        rvVideoList.adapter = videoListAdapter

        binding.tvBtnVideoListTotal.setOnClickListener {
            performClick(it)
        }
        binding.tvBtnVideoListDriving.setOnClickListener {
            performClick(it)
        }
        binding.tvBtnVideoListParking.setOnClickListener {
            performClick(it)
        }
        binding.tvBtnVideoListEvent.setOnClickListener {
            performClick(it)
        }
        setSelectedBtn(binding.tvBtnVideoListTotal)
    }

    private fun performClick(view: View) {
        setSelectedBtn(view)
        viewModel.getVideoList()
    }

    private fun setSelectedBtn(view: View) {
        setSelectedBtnAll(false)
        view.isSelected = true

        when (view) {
            binding.tvBtnVideoListTotal -> {
                viewModel.setVideoType(VideolistViewModel.VideoType.TOTAL)
            }
            binding.tvBtnVideoListDriving -> {
                viewModel.setVideoType(VideolistViewModel.VideoType.DRIVING)
            }
            binding.tvBtnVideoListParking -> {
                viewModel.setVideoType(VideolistViewModel.VideoType.PARKING)
            }
            binding.tvBtnVideoListEvent -> {
                viewModel.setVideoType(VideolistViewModel.VideoType.EVENT)
            }
        }
    }

    private fun setSelectedBtnAll(isSelected: Boolean) {
        _binding?.tvBtnVideoListTotal?.isSelected = isSelected
        _binding?.tvBtnVideoListDriving?.isSelected = isSelected
        _binding?.tvBtnVideoListParking?.isSelected = isSelected
        _binding?.tvBtnVideoListEvent?.isSelected = isSelected
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}