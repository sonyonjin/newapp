package com.skyautonet.seda_aiv.ui.videostorage

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.common.CustomDialog
import com.skyautonet.seda_aiv.data.source.local.file.VideoFile
import com.skyautonet.seda_aiv.databinding.FragmentVideoStorageBinding
import com.skyautonet.seda_aiv.ui.BaseFragment
import com.skyautonet.seda_aiv.ui.fullvideo.FullVideoActivity

class VideoStorageFragment : BaseFragment() {

    private var _binding: FragmentVideoStorageBinding? = null
    private lateinit var viewModel: VideoStorageViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var rvVideoList: RecyclerView
    private lateinit var srVideoList: SwipeRefreshLayout
    private lateinit var videoStorageAdapter: VideoStorageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this).get(VideoStorageViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_video_storage, container, false)
        _binding = FragmentVideoStorageBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }
        initView()
        viewModel._videoList.observe(viewLifecycleOwner) {
            videoStorageAdapter.setList(viewModel.videoList)
            binding.tvVideoStorageTotal.text = resources.getString(R.string.video_total_count, viewModel.getTotalCount())
            videoStorageAdapter.notifyDataSetChanged()
        }
        viewModel.getVideoStorage()
        return view
    }

    private fun initView() {
        binding.tvBtnVideoStorageDelete.isSelected = true
        binding.tvBtnVideoStorageDelete.setOnClickListener {
            CustomDialog(
                requireContext(),
                resources.getString(R.string.video_storage_delete_dialog_msg),
                R.drawable.ic_dialog_delete,
                resources.getString(R.string.btn_ok),
                resources.getString(R.string.btn_cancel),
                object : CustomDialog.IOnClickItemListener {
                    override fun onClickConfirm(dialog: Dialog) {
                        viewModel.deleteVideoFile(videoStorageAdapter.getDeleteItemList())
                        dialog.dismiss()
                    }
                }).show()
        }

        rvVideoList = binding.rvVideoStorage
        rvVideoList.layoutManager = LinearLayoutManager(activity)

        srVideoList = binding.srVideoStorage
        srVideoList.setOnRefreshListener {
            viewModel.getVideoStorage()
            srVideoList.isRefreshing = false
        }

        videoStorageAdapter = VideoStorageAdapter(requireContext(), viewModel.videoList, rvVideoList, object :
            VideoStorageAdapter.VideoItemClick {
            override fun onPlay(viewHolder: VideoStorageAdapter.ViewHolder) {
                if (viewHolder.itemView.tag is VideoFile) {
                    val videoFile = viewHolder.itemView.tag as VideoFile

                    val videoIntent = Intent(requireContext(), FullVideoActivity::class.java)
                    videoIntent.putExtra(FullVideoActivity.VIDEO_URI, videoFile.file.toUri().toString())
                    startActivity(videoIntent)
                }
            }

            override fun onDeleteCheckbox(viewHolder: VideoStorageAdapter.ViewHolder) {
                if (viewHolder.itemView.tag is VideoFile) {
                    val videoFile = viewHolder.itemView.tag as VideoFile
                    videoFile.isDeleteSelected = !videoFile.isDeleteSelected

                    if (viewHolder.ivDelete.tag is Int) {
                        val position = viewHolder.ivDelete.tag as Int
                        videoStorageAdapter.notifyItemChanged(position)
                    }
                }
            }
        })
        rvVideoList.adapter = videoStorageAdapter

        binding.ivDelete.setOnClickListener {
            videoStorageAdapter.allSelectDeleteItem()

        }

        binding.tvBtnVideoStorageTotal.setOnClickListener {
            performClick(it)
        }
        binding.tvBtnVideoStorageDriving.setOnClickListener {
            performClick(it)
        }
        binding.tvBtnVideoStorageParking.setOnClickListener {
            performClick(it)
        }
        binding.tvBtnVideoStorageEvent.setOnClickListener {
            performClick(it)
        }
        setSelectedBtn(binding.tvBtnVideoStorageTotal)
    }

    private fun performClick(view: View) {
        setSelectedBtn(view)
        viewModel.getVideoStorage()
    }

    private fun setSelectedBtn(view: View) {
        setSelectedBtnAll(false)
        view.isSelected = true

        when (view) {
            binding.tvBtnVideoStorageTotal -> {
                viewModel.setVideoType(VideoFile.VideoType.TOTAL)
            }
            binding.tvBtnVideoStorageDriving -> {
                viewModel.setVideoType(VideoFile.VideoType.DRIVING)
            }
            binding.tvBtnVideoStorageParking -> {
                viewModel.setVideoType(VideoFile.VideoType.PARKING)
            }
            binding.tvBtnVideoStorageEvent -> {
                viewModel.setVideoType(VideoFile.VideoType.EVENT)
            }
        }
    }

    private fun setSelectedBtnAll(isSelected: Boolean) {
        _binding?.tvBtnVideoStorageTotal?.isSelected = isSelected
        _binding?.tvBtnVideoStorageDriving?.isSelected = isSelected
        _binding?.tvBtnVideoStorageParking?.isSelected = isSelected
        _binding?.tvBtnVideoStorageEvent?.isSelected = isSelected
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}