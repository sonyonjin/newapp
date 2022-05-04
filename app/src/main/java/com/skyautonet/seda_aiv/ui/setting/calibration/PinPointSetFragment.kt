package com.skyautonet.seda_aiv.ui.setting.calibration

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.data.ResultObj
import com.skyautonet.seda_aiv.databinding.FragmentPinPointSetBinding
import com.skyautonet.seda_aiv.model.PointerModel
import com.skyautonet.seda_aiv.ui.BaseFragment
import com.skyautonet.seda_aiv.ui.MainActivity
import com.skyautonet.seda_aiv.ui.setting.SettingFragment

class PinPointSetFragment : BaseFragment() {

    companion object {
        const val CAMERA_IMAGE_WIDTH = 1280
        const val CAMERA_IMAGE_HEIGHT = 720
        private var actualImageWidth = 0
        private var actualImageHeight = 0
    }

    private var _binding: FragmentPinPointSetBinding? = null
    private lateinit var viewModel: PinPointSetViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var buttonLock = true
    private var needInit = true
    private var needInitFirstPointer = true

    private var pointerMap = HashMap<ImageView, PointerModel>()
    private var ivSelectedPinPointer: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this).get(PinPointSetViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_pin_point_set, container, false)
        _binding = FragmentPinPointSetBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }
        viewModel.observeCalibrationList.observe(viewLifecycleOwner) {
            if (it is ResultObj.Success) {
                for (i in 0 until it.data.size) {
                    val calibrationItem = it.data.get(i)
                    calibrationItem.id = i
                    viewModel.downloadCalibration(calibrationItem, object:
                        PinPointSetViewModel.ImageDownloadListener {
                        override fun onDownloadCompleted(isSuccess: Boolean) {
                            if (isSuccess) {
                                viewModel.convertImage(calibrationItem, CAMERA_IMAGE_WIDTH, CAMERA_IMAGE_HEIGHT)
                            } else{
                                Toast.makeText(requireContext(), "Download failed", Toast.LENGTH_SHORT).show()
                            }
                        }

                    })
                }

            } else {
                buttonLock = false
            }
        }

        viewModel.photoImage.observe(viewLifecycleOwner) { bitmap ->
            _binding?.ivPhoto?.setImageBitmap(bitmap)
            _binding?.ivZoomView?.let { ivZoomView ->
                ivZoomView.setImage(bitmap)
            }
        }

        needInit = true
        initView(view)
        toEnablePointerBtn(false)
        viewModel.getCalibrationList()
        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView(rootView: View) {
        binding.ivPhoto.viewTreeObserver.addOnGlobalLayoutListener {
            if (needInit && _binding?.ivPhoto?.drawable != null) {
                needInit = false
                _binding?.ivPhoto?.drawable?.bounds?.let { rect ->
                    val layoutWidth = _binding!!.ivPhoto.width
                    val layoutHeight = _binding!!.ivPhoto.height
                    val scaleX = layoutWidth.toFloat() / rect.width().toFloat()
                    val scaleY = layoutHeight.toFloat() / rect.height().toFloat()
                    val scale = Math.min(scaleX, scaleY)

                    actualImageWidth = (scale * rect.width()).toInt()
                    actualImageHeight = (scale * rect.height()).toInt()

                    _binding?.chpvPhotoPaintCanvas?.layoutParams?.width = actualImageWidth
                    _binding?.chpvPhotoPaintCanvas?.layoutParams?.height = actualImageHeight

                    // pointerMap초기화
                    for (i in pointerMap) {
                        i.value.setSize(
                            layoutWidth,
                            layoutHeight,
                            scale,
                            actualImageWidth,
                            actualImageHeight
                        )
                        i.value.setTouchPoint(i.value.pointF.x, i.value.pointF.y, true)
                    }
                    toEnablePointerBtn(true)
                }

                // 기본치로 1번 포인터 선택
                selectPointer(binding.ivPinPointer1)
            }
        }

        val touchListener = View.OnTouchListener { view, motionEvent ->
            selectPointer(view)
            false
        }
        pointerMap.put(binding.ivPinPointer1, PointerModel(1, binding.ivPinPointer1))
        pointerMap.put(binding.ivPinPointer2, PointerModel(2, binding.ivPinPointer2))
        pointerMap.put(binding.ivPinPointer3, PointerModel(3, binding.ivPinPointer3))
        pointerMap.put(binding.ivPinPointer4, PointerModel(4, binding.ivPinPointer4))
        pointerMap.put(binding.ivPinPointer5, PointerModel(5, binding.ivPinPointer5))
        pointerMap.put(binding.ivPinPointer6, PointerModel(6, binding.ivPinPointer6))
        pointerMap.put(binding.ivPinPointer7, PointerModel(7, binding.ivPinPointer7))
        pointerMap.put(binding.ivPinPointer8, PointerModel(8, binding.ivPinPointer8))
        binding.ivPinPointer1.setOnTouchListener(touchListener)
        binding.ivPinPointer2.setOnTouchListener(touchListener)
        binding.ivPinPointer3.setOnTouchListener(touchListener)
        binding.ivPinPointer4.setOnTouchListener(touchListener)
        binding.ivPinPointer5.setOnTouchListener(touchListener)
        binding.ivPinPointer6.setOnTouchListener(touchListener)
        binding.ivPinPointer7.setOnTouchListener(touchListener)
        binding.ivPinPointer8.setOnTouchListener(touchListener)

        binding.chpvPhotoPaintCanvas.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {

                }
                MotionEvent.ACTION_MOVE -> {
                    ivSelectedPinPointer?.let { pointerView ->
                        pointerMap.get(pointerView)?.let { pointerModel ->
                            pointerModel.setTouchPoint(motionEvent.x, motionEvent.y, false)
                            setZoomViewPosition()
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                }
            }
            false
        }

        binding.ivZoomViewBtnLeft.setOnClickListener {
            ivSelectedPinPointer?.let {
                pointerMap.get(it)?.let { pointerModel ->
                    pointerModel.setTouchPoint(it.x - 1, it.y, true)
                }
            }
        }
        binding.ivZoomViewBtnRight.setOnClickListener {
            ivSelectedPinPointer?.let {
                pointerMap.get(it)?.let { pointerModel ->
                    pointerModel.setTouchPoint(it.x + 1, it.y, true)
                }
            }
        }
        binding.ivZoomViewBtnTop.setOnClickListener {
            ivSelectedPinPointer?.let {
                pointerMap.get(it)?.let { pointerModel ->
                    pointerModel.setTouchPoint(it.x, it.y - 1, true)
                }
            }
        }
        binding.ivZoomViewBtnBottom.setOnClickListener {
            ivSelectedPinPointer?.let {
                pointerMap.get(it)?.let { pointerModel ->
                    pointerModel.setTouchPoint(it.x, it.y + 1, true)
                }
            }
        }

        binding.tvPinPointSetBtnPreview.setOnClickListener {
            onPrevBtnSelected()
        }
        binding.tvPinPointSetBtnNext.setOnClickListener {
            if (!buttonLock) {
                onNextBtnSelected()
            }
        }
    }

    private fun toEnablePointerBtn(isEnable: Boolean) {
        for (i in pointerMap) {
            i.key.isEnabled = isEnable
        }
        _binding?.ivZoomViewBtnLeft?.isEnabled = isEnable
        _binding?.ivZoomViewBtnRight?.isEnabled = isEnable
        _binding?.ivZoomViewBtnTop?.isEnabled = isEnable
        _binding?.ivZoomViewBtnBottom?.isEnabled = isEnable
    }

    private fun selectPointer(view: View) {
        setSelectedImgAll(false)
        view.isSelected = true
        if (view is ImageView) {
            ivSelectedPinPointer = view
        }
        setZoomViewPosition()
    }

    private fun setZoomViewPosition() {
        pointerMap.get(ivSelectedPinPointer)?.let { pointerModel ->
            _binding?.ivZoomView?.let { ivZoomView ->
                ivZoomView.setPosition(pointerModel.touchPointF.x, pointerModel.touchPointF.y)
            }
        }
    }

    private fun setSelectedImgAll(isSelected: Boolean) {
        for (i in pointerMap) {
            i.key.isSelected = isSelected
        }
    }

    private fun onPrevBtnSelected() {
        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity
            val fragment = SettingFragment()
            val bundle = Bundle()
            bundle.putInt(SettingFragment.SETTING_MENU_SELECT, SettingFragment.SETTING_MENU.CALIBRATION.getPosition())
            bundle.putInt(SettingFragment.TARGET_SCREEN, SettingFragment.CALIBRATION_TARGET_SCREEN.CAMERA_CHANNEL_SELECT.getPosition())
            fragment.arguments = bundle
            mainActivity.releaseFullScreen(fragment)
        }
    }

    private fun onNextBtnSelected() {
        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity
            mainActivity.loadFullScreen(CarSizeInputFragment())
        }
    }
}