package com.skyautonet.seda_aiv.ui.fullvideo

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.databinding.ActivityFullVideoBinding
import com.skyautonet.seda_aiv.ui.BaseActivity
import com.skyautonet.seda_aiv.util.SdCardUtil
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout
import java.io.FileDescriptor

class FullVideoActivity : BaseActivity() {

    companion object {
        const val VIDEO_URI = "video_uri"
    }

    private lateinit var binding: ActivityFullVideoBinding

    private lateinit var libVlc: LibVLC
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var videoLayout: VLCVideoLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullVideoBinding.inflate(layoutInflater)

        /* FullScreen Activity */
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        videoLayout = binding.vlcVideoLayout
        // Create LibVLC
        libVlc = LibVLC(this)
        mediaPlayer = MediaPlayer(libVlc)
        mediaPlayer.attachViews(videoLayout, null, false, false)
        mediaPlayer.setEventListener { event ->
            when (event.type) {
                MediaPlayer.Event.Playing -> {
                }
            }

        }
    }

    override fun onStart() {
        super.onStart()

        val uriString = intent.getStringExtra(VIDEO_URI)
        if (!TextUtils.isEmpty(uriString)) {
            val fd: FileDescriptor? = getContentResolver()
                .openFileDescriptor(Uri.parse(uriString), "r")
                ?.getFileDescriptor()

            fd?.let {
                Media(libVlc, it).apply {
                    setHWDecoderEnabled(true, false)
                    mediaPlayer.media = this
                }.release()
                mediaPlayer.play()
            }
        }

    }
}