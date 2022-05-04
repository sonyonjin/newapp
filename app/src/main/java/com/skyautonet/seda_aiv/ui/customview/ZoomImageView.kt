package com.skyautonet.seda_aiv.ui.customview

import androidx.appcompat.widget.AppCompatImageView
import android.view.View.OnTouchListener
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.util.AttributeSet
import android.view.View

class ZoomImageView : AppCompatImageView, OnTouchListener {

    companion object {
        private const val MODE_NONE = 0
        private const val MODE_DRAG = 1
        private const val MODE_ZOOM = 2
        private const val MATRIX_VALUES_NUM = 9
        private const val DEFAULT_SCALE = 2.0f
    }

    private var getBitmap_: Bitmap? = null
    private val movePoint_ = PointF()
    private val imageMatrix_ = Matrix()
    private val tempImageMatrix_ = Matrix()
    private var span_ = 0.0f
    private var initialScale_ = DEFAULT_SCALE
    private var maxScale_ = 10.0f
    private val midPoint_ = PointF()
    private var mode_ = MODE_NONE
    private var blockHeight_ = 0

    var savedImageMatrix = Matrix()
        private set

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context,
        attrs,
        defStyle) {
        init(context)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(context: Context) {
        super.setOnTouchListener(this)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        var ret: Boolean
        val actionCode = event.action and MotionEvent.ACTION_MASK

        ret = onTouchDragEvent(event, actionCode)
        if (!ret) {
            ret = onTouchPointerEvent(event, actionCode)
        }
        return ret
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (super.getWidth() == 0) {
            return
        }

        drawInitial()
    }

    override fun onDraw(canvas: Canvas) {

        if (getBitmap_ == null) return
        var matrix = super.getImageMatrix()
        val values = FloatArray(MATRIX_VALUES_NUM)

        if (mode_ == MODE_ZOOM) {
            super.onDraw(canvas)
            matrix = super.getImageMatrix()

            canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_OVER)
        }
        matrix.getValues(values)

        chkXPosition(getBitmap_!!, values[Matrix.MSCALE_X], matrix)
        chkYPosition(getBitmap_!!, values[Matrix.MSCALE_Y], matrix)

        super.setImageMatrix(matrix)
        savedImageMatrix = super.getImageMatrix()
        super.onDraw(canvas)
    }

    fun setImage(bitmap: Bitmap?) {
        if (bitmap == null) return
        if (getBitmap_ != null) {
            super.setImageBitmap(null)
        }
        getBitmap_ = bitmap
        if (super.getWidth() == 0) return

        drawInitial()
    }

    fun setMaxScale(scale: Float) {
        if (initialScale_ > scale) {
            return
        }
        maxScale_ = scale
    }

    private fun setMode(mode: Int) {
        mode_ = mode
    }

    private fun drawInitial() {
        if (getBitmap_ == null) return
        super.setImageBitmap(getBitmap_)
        val values = FloatArray(MATRIX_VALUES_NUM)
        imageMatrix_.getValues(values)
        imageMatrix_.postScale(initialScale_, initialScale_)
        tempImageMatrix_.set(imageMatrix_)
        setCenteringY(getBitmap_!!, initialScale_, imageMatrix_)
        setValueToImageMatrix(Matrix.MTRANS_X, 0f, imageMatrix_)

        super.setImageMatrix(imageMatrix_)
    }

    fun setPosition(touchPointX: Float, touchPointY: Float) {
        imageMatrix_.postTranslate(-touchPointX, -touchPointY)
        super.setImageMatrix(imageMatrix_)
    }

    private fun onTouchDragEvent(event: MotionEvent, actionCode: Int): Boolean {
        var ret = false
        when (actionCode) {
            MotionEvent.ACTION_DOWN -> {
                actionDown(event)
                setMode(MODE_DRAG)
                ret = true
            }
            MotionEvent.ACTION_MOVE -> if (mode_ == MODE_DRAG) {
                actionMove(event)
                movePoint_[event.x] = event.y
                ret = true
            }
            MotionEvent.ACTION_UP -> {
                setMode(MODE_NONE)
                tempImageMatrix_.set(super.getImageMatrix())
                ret = true
            }
            else -> {}
        }
        return ret
    }

    private fun onTouchPointerEvent(event: MotionEvent, actionCode: Int): Boolean {
        var ret = false
        when (actionCode) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                actionPointerDown(event)
                setMode(MODE_ZOOM)
                ret = true
            }
            MotionEvent.ACTION_MOVE -> if (mode_ == MODE_ZOOM) {
                ret = actionPointerMove(event)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                setMode(MODE_NONE)
                tempImageMatrix_.set(super.getImageMatrix())
                ret = true
            }
            else -> {}
        }
        return ret
    }

    private fun setValueToImageMatrix(index: Int, value: Float, dst: Matrix) {
        val values = FloatArray(MATRIX_VALUES_NUM)
        dst.getValues(values)
        values[index] = value
        dst.setValues(values)
    }

    private fun setCenteringY(bitmap: Bitmap, scale: Float, matrix: Matrix) {
        val viewHeight = super.getHeight().toFloat()
        var imageHeight = bitmap.height.toFloat()
        imageHeight *= scale
        val values = FloatArray(MATRIX_VALUES_NUM)
        matrix.getValues(values)

        var cal = viewHeight - imageHeight
        if (cal > 0) {
            cal /= 2.0f
            setValueToImageMatrix(Matrix.MTRANS_Y, cal, matrix)
        }
    }

    private fun chkXPosition(bitmap: Bitmap, scale: Float, matrix: Matrix) {
        val viewWidth = super.getWidth().toFloat()
        var imageWidth = bitmap.width.toFloat()
        imageWidth *= scale
        val values = FloatArray(MATRIX_VALUES_NUM)
        matrix.getValues(values)
        val currentX = values[Matrix.MTRANS_X]
        if (currentX > 0) {
            setValueToImageMatrix(Matrix.MTRANS_X, 0f, matrix)
        } else if (imageWidth + currentX < viewWidth) {
            val cal = values[Matrix.MTRANS_X] + (viewWidth - (imageWidth + currentX))
            setValueToImageMatrix(Matrix.MTRANS_X, cal, matrix)
        }
    }

    private fun chkYPosition(bitmap: Bitmap, scale: Float, matrix: Matrix) {
        val viewHeight = super.getHeight().toFloat()
        var imageHeight = bitmap.height.toFloat()
        imageHeight *= scale
        val values = FloatArray(MATRIX_VALUES_NUM)
        matrix.getValues(values)
        val currentY = values[Matrix.MTRANS_Y]

        if (currentY > blockHeight_) {
            setValueToImageMatrix(Matrix.MTRANS_Y, blockHeight_.toFloat(), matrix)
        } else if (imageHeight + currentY < viewHeight - blockHeight_) {
            val cal =
                values[Matrix.MTRANS_Y] + (viewHeight - (imageHeight + currentY) - blockHeight_)
            setValueToImageMatrix(Matrix.MTRANS_Y, cal, matrix)
        }
    }

    fun setBlockHeight(blockHeight: Int) {
        blockHeight_ = blockHeight
    }

    private fun actionDown(event: MotionEvent) {

        movePoint_[event.x] = event.y
        imageMatrix_.set(tempImageMatrix_)
    }

    private fun actionMove(event: MotionEvent) {
        val current = PointF(event.x, event.y)

        val deltaX = current.x - movePoint_.x
        val deltaY = current.y - movePoint_.y

        imageMatrix_.postTranslate(deltaX, deltaY)
        super.setImageMatrix(imageMatrix_)
    }

    private fun actionPointerDown(event: MotionEvent): Boolean {
        val span = getSpan(event)
        if (span < 10.0f) {
            return false
        }
        span_ = span

        val deltaX = event.getX(0) + event.getX(1)
        val deltaY = event.getY(0) + event.getY(1)
        midPoint_[deltaX / 2.0f] = deltaY / 2.0f
        tempImageMatrix_.set(super.getImageMatrix())
        return true
    }

    private fun actionPointerMove(event: MotionEvent): Boolean {
        imageMatrix_.set(tempImageMatrix_)
        val currentScale = getMatrixScale(imageMatrix_)
        val scale = getScale(event)
        val tmpScale = scale * currentScale
        if (tmpScale < initialScale_) {
            return false
        }
        if (tmpScale > maxScale_) {
            return false
        }

        imageMatrix_.postScale(scale, scale, midPoint_.x, midPoint_.y)
        super.setImageMatrix(imageMatrix_)
        return true
    }

    private fun getScale(event: MotionEvent): Float {
        val span = getSpan(event)
        return span / span_
    }

    private fun getMatrixScale(matrix: Matrix): Float {
        val values = FloatArray(MATRIX_VALUES_NUM)
        matrix.getValues(values)
        val currentScale = values[Matrix.MSCALE_X]
        return if (currentScale == 0f) {
            1f
        } else currentScale
    }

    private fun getSpan(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

}