package com.skyautonet.seda_aiv.ui.fullvideo

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.view.WindowManager
import android.widget.MediaController
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.databinding.ActivityFullVideoBinding
import com.skyautonet.seda_aiv.ui.BaseActivity

class FullVideoActivity : BaseActivity() {

    companion object {
        const val VIDEO_URI = "video_uri"
    }

    private lateinit var binding: ActivityFullVideoBinding
    var mediaController: MediaController? = null

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
        val uriString = intent.getStringExtra(VIDEO_URI)
        if (!TextUtils.isEmpty(uriString)) {
            binding.videoView.setVideoURI(Uri.parse(uriString))

            mediaController = MediaController(this)
            mediaController!!.setMediaPlayer(binding.videoView)
            binding.videoView.setMediaController(mediaController!!)

            binding.videoView.setOnPreparedListener(object: MediaPlayer.OnPreparedListener {
                override fun onPrepared(p0: MediaPlayer?) {
                    binding.videoView.start()

                    p0?.setOnCompletionListener(object: MediaPlayer.OnCompletionListener {
                        override fun onCompletion(p0: MediaPlayer?) {
                            finish()
                        }

                    })
                }

            })
        }
    }
}