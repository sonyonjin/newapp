package com.skyautonet.seda_aiv.ui.liveview

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.constraintlayout.widget.ConstraintLayout
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

    private var isInitLiveMenu = false
    private lateinit var liveMenuBtnTwoD: ConstraintLayout
    private lateinit var liveMenuBtnThreeD: ConstraintLayout
    private lateinit var liveMenuBtnRear: ConstraintLayout
    private lateinit var liveMenuBtnRearRight: ConstraintLayout
    private lateinit var liveMenuBtnRearLeft: ConstraintLayout
    private lateinit var liveMenuBtnFront: ConstraintLayout
    private var isEnable3D = false

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
            add("--drop-late-frames")
            add("--skip-frames")
            add("--rtsp-tcp")
            add("-vvv")
        })
        mediaPlayer = MediaPlayer(libVlc)
        mediaPlayer.attachViews(videoLayout, null, false, false)
        mediaPlayer.setEventListener { event ->
            when (event.type) {
                MediaPlayer.Event.Playing -> {
                    _binding?.clBgAlert?.visibility = View.GONE
                    _binding?.clBgCameraPreview?.visibility = View.GONE
                    _binding?.ivTruck?.visibility = View.GONE
                }
            }

        }

        liveMenuBtnTwoD = binding.clLiveMenuBtnTwoD
        liveMenuBtnThreeD = binding.clLiveMenuBtnThreeD
        liveMenuBtnRear = binding.clLiveMenuBtnRear
        liveMenuBtnRearRight = binding.clLiveMenuBtnRearRight
        liveMenuBtnRearLeft = binding.clLiveMenuBtnRearLeft
        liveMenuBtnFront = binding.clLiveMenuBtnFront
        binding.clLiveMenuBtnTwoD.viewTreeObserver.addOnGlobalLayoutListener {
            if (!isInitLiveMenu) {
                isInitLiveMenu = true
                setSelectedMenu(liveMenuBtnTwoD)
                setSlideMenu(false, 0) //for collapse at first screen
            }
        }

        binding.clLiveMenuBtnTwoD.setOnClickListener {
            if (isEnable3D) {
                setSelectedMenu(it)
            }
        }
        binding.clLiveMenuBtnThreeD.setOnClickListener {
            if (!isEnable3D) {
                setSelectedMenu(it)
            }
        }
        binding.clLiveMenuBtnRear.setOnClickListener {
            setSelected3DMenu(it)
        }
        binding.clLiveMenuBtnRearRight.setOnClickListener {
            setSelected3DMenu(it)
        }
        binding.clLiveMenuBtnRearLeft.setOnClickListener {
            setSelected3DMenu(it)
        }
        binding.clLiveMenuBtnFront.setOnClickListener {
            setSelected3DMenu(it)
        }

        binding.btnLiveMenuSlide.setOnClickListener {
            setSlideMenu(isExpanded(), 500)
        }
        binding.btnLiveMenuSlideDummy.setOnClickListener {
            setSlideMenu(isExpanded(), 500)
        }
    }

    private fun isExpanded(): Boolean {
        return _binding?.ivExpandMenu?.visibility ?: View.INVISIBLE == View.VISIBLE
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

//        binding.wvVideoLayout.loadUrl("http://192.168.0.210:5000/")
//        _binding?.clBgAlert?.visibility = View.GONE
//        _binding?.clBgCameraPreview?.visibility = View.GONE
//        _binding?.ivTruck?.visibility = View.GONE

//        viewModel.startAlert()
    }

    override fun onStop() {
        super.onStop()

        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.detachViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setSlideMenu(isExpand: Boolean, duration: Long) {
        val animation = if (isExpand) AlphaAnimation(0F, 1F) else AlphaAnimation(1F, 0F)
        animation.duration = duration
        animation.fillAfter = true
        _binding?.clLiveMenuItem?.startAnimation(animation)

        setEnableMenuAll(isExpand)

        val animate = TranslateAnimation(
            0F,
            0F,
            if (isExpand) _binding?.clLiveMenuItem?.height?.toFloat() ?: 0F else 0F,
            if (isExpand) 0F else _binding?.clLiveMenuItem?.height?.toFloat() ?: 0F
        )

        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                if (isExpand) {
                    _binding?.ivCollapseMenu?.visibility = View.VISIBLE
                    _binding?.ivExpandMenu?.visibility = View.INVISIBLE
                    _binding?.btnLiveMenuSlideDummy?.visibility = View.GONE
                } else {
                    _binding?.ivCollapseMenu?.visibility = View.INVISIBLE
                    _binding?.ivExpandMenu?.visibility = View.VISIBLE
                    _binding?.btnLiveMenuSlideDummy?.visibility = View.VISIBLE
                }
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
        animate.duration = duration
        animate.fillAfter = true
        _binding?.clLiveMenu?.startAnimation(animate)
    }

    private fun setSelectedMenu(view: View) {
        if (view == liveMenuBtnTwoD) {
            isEnable3D = false
            setSelectedMenuAll(false)
            setSelectedMenu3DAll(false)
            setEnableMenu3DAll(false)
        } else {
            isEnable3D = true
            setSelectedMenuAll(false)
            setEnableMenu3DAll(true)
        }

        setSelectedView(view)
    }

    private fun setSelected3DMenu(view: View) {
        setSelectedMenu3DAll(false)
        setSelectedView(view)
    }

    private fun setSelectedView(view: View) {
        view.isSelected = true
        if (view.isEnabled) {
            when (view) {
                liveMenuBtnTwoD -> {
                    viewModel.setViewModel(LiveViewViewModel.ViewMode.TWO_D)
                }
                liveMenuBtnThreeD -> {
                    // 3D모드시 기본치뷰모드
                    setSelectedView(liveMenuBtnRear)
                }
                liveMenuBtnRear -> {
                    viewModel.setViewModel(LiveViewViewModel.ViewMode.THREE_D_REAR)
                }
                liveMenuBtnRearRight -> {
                    viewModel.setViewModel(LiveViewViewModel.ViewMode.THREE_D_REAR_RIGHT)
                }
                liveMenuBtnRearLeft -> {
                    viewModel.setViewModel(LiveViewViewModel.ViewMode.THREE_D_REAR_LEFT)
                }
                liveMenuBtnFront -> {
                    viewModel.setViewModel(LiveViewViewModel.ViewMode.THREE_D_FRONT)
                }
            }
        }
    }

    private fun setSelectedMenuAll(isSelected: Boolean) {
        _binding?.clLiveMenuBtnTwoD?.isSelected = isSelected
        _binding?.clLiveMenuBtnThreeD?.isSelected = isSelected
    }

    private fun setSelectedMenu3DAll(isSelected: Boolean) {
        _binding?.clLiveMenuBtnRear?.isSelected = isSelected
        _binding?.clLiveMenuBtnRearRight?.isSelected = isSelected
        _binding?.clLiveMenuBtnRearLeft?.isSelected = isSelected
        _binding?.clLiveMenuBtnFront?.isSelected = isSelected
    }

    private fun setEnableMenuAll(isEnable: Boolean) {
        _binding?.clLiveMenuBtnTwoD?.isEnabled = isEnable
        _binding?.clLiveMenuBtnThreeD?.isEnabled = isEnable

        // 3D모드가 아니면 3D방향버튼은 비활성화시킴
        if (isEnable && !isEnable3D) {
            _binding?.clLiveMenuBtnRear?.isEnabled = false
            _binding?.clLiveMenuBtnRearRight?.isEnabled = false
            _binding?.clLiveMenuBtnRearLeft?.isEnabled = false
            _binding?.clLiveMenuBtnFront?.isEnabled = false
        }
    }

    private fun setEnableMenu3DAll(isEnable: Boolean) {
        _binding?.clLiveMenuBtnRear?.isEnabled = isEnable
        _binding?.clLiveMenuBtnRearRight?.isEnabled = isEnable
        _binding?.clLiveMenuBtnRearLeft?.isEnabled = isEnable
        _binding?.clLiveMenuBtnFront?.isEnabled = isEnable
    }
}