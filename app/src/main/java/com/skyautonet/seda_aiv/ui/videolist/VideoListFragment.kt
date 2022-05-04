package com.skyautonet.seda_aiv.ui.videolist

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.common.CustomDialog
import com.skyautonet.seda_aiv.data.ResultObj
import com.skyautonet.seda_aiv.data.source.local.file.VideoFile
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
            if (it is ResultObj.Success) {
                viewModel.syncDownloadedVideoFile()
            }
        }
        viewModel.syncedDownloadedVideoFileObserve.observe(viewLifecycleOwner) {
            "${resources.getString(R.string.video_total_count)} ${viewModel.getTotalCount()}".also { binding.tvVideoListTotal.text = it }
            videoListAdapter.setList(viewModel.videoList)
            videoListAdapter.notifyDataSetChanged()
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

        videoListAdapter = VideoListAdapter(requireContext(), viewModel.videoList, rvVideoList, object :
            VideoListAdapter.VideoItemClick {
            override fun onDownload(viewHolder: VideoListAdapter.ViewHolder) {
                if (viewHolder.itemView.tag is VideoItem) {
                    val videoItem = viewHolder.itemView.tag as VideoItem

                    CustomDialog(
                        requireContext(),
                        resources.getString(R.string.video_list_download_dialog_msg),
                        R.drawable.ic_dialog_download,
                        resources.getString(R.string.btn_ok),
                        resources.getString(R.string.btn_cancel),
                        object : CustomDialog.IOnClickItemListener {
                            override fun onClickConfirm(dialog: Dialog) {
                                dialog.dismiss()
                                showLoader()
                                viewModel.downloadVideo(videoItem.file_name,
                                    object : VideolistViewModel.VideoItemDownloadListener {
                                        override fun onDownloadCompleted(isSuccess: Boolean) {
                                            if (isSuccess) {
                                                videoListAdapter.setDisableItem(viewHolder)
                                            } else{
                                                Toast.makeText(requireContext(), "Download failed", Toast.LENGTH_SHORT).show()
                                            }
                                            hideLoader()
                                        }
                                    })
                            }
                        }).show()
                }
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
                viewModel.setVideoType(VideoFile.VideoType.TOTAL)
            }
            binding.tvBtnVideoListDriving -> {
                viewModel.setVideoType(VideoFile.VideoType.DRIVING)
            }
            binding.tvBtnVideoListParking -> {
                viewModel.setVideoType(VideoFile.VideoType.PARKING)
            }
            binding.tvBtnVideoListEvent -> {
                viewModel.setVideoType(VideoFile.VideoType.EVENT)
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