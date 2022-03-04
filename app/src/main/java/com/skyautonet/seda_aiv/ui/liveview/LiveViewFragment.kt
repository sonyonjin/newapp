package com.skyautonet.seda_aiv.ui.liveview

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import androidx.lifecycle.ViewModelProvider
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.databinding.FragmentLiveViewBinding
import com.skyautonet.seda_aiv.ui.BaseFragment
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout
import java.lang.Exception

class LiveViewFragment : BaseFragment() {

    private var _binding: FragmentLiveViewBinding? = null
    private lateinit var viewModel: LiveViewViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var libVlc: LibVLC
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var videoLayout: VLCVideoLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(LiveViewViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_live_view, container, false)
        _binding = FragmentLiveViewBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }
        initView()
        return view
    }

    private fun initView() {
        videoLayout = binding.vlcVideoLayout
        // Create LibVLC
        libVlc = LibVLC(context, ArrayList<String>().apply {
//            add("--no-drop-late-frames")
//            add("--no-skip-frames")
            add("--rtsp-tcp")
            add("-vvv")
        })
        mediaPlayer = MediaPlayer(libVlc)
        mediaPlayer.attachViews(videoLayout, null, false, false)

    }

    override fun onStart() {
        super.onStart()

        try {
            if (!TextUtils.isEmpty(viewModel.appConfig.value?.rtspLink1)) {
                Media(libVlc, Uri.parse(viewModel.appConfig.value?.rtspLink1)).apply {
                    setHWDecoderEnabled(true, false)
                    addOption(":network-caching=0")
                    addOption(":clock-jitter=0")
                    addOption(":clock-synchro=0")
                    mediaPlayer.media = this
                }.release()
                mediaPlayer.play()
            }
        } catch (e: Exception) {
            Log.e("SeDA_AIV", e.message ?: e.stackTraceToString())
        }

        viewModel.startAlert()
    }

    override fun onStop() {
        super.onStop()

        mediaPlayer.stop()
        mediaPlayer.detachViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}