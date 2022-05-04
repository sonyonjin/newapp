package com.skyautonet.seda_aiv.ui.customview

import com.skyautonet.seda_aiv.model.DrawPathModel
import kotlin.jvm.Synchronized
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.ArrayList
import java.util.HashMap

class CanvasHandPaintView(context: Context?, attributeSet: AttributeSet?) :
    View(context, attributeSet) {
    private var strokeWidth_ = 2
    private val paint_: Paint
    private var singlePath_: Path? = null
    private var allPath_: Path
    private var canvas_: Canvas? = null
    private var arrayPathList_: MutableList<Path?>? = null
    private var pathMap_: MutableMap<Int, DrawPathModel>
    private var nowPathNo_ = 0

    init {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas_ = canvas
        drawLines(false)
    }

    fun setStrokeWidth(size: Int) {
        //this.strokeWidth_ = size;
        nowPathNo_++
        paint_.strokeWidth = size.toFloat()
        allPath_ = Path()
        arrayPathList_ = ArrayList() //reset
        pathMap_[nowPathNo_] = DrawPathModel(size, null)
    }

    private fun addPath() {
        val max = arrayPathList_!!.size
        for (i in 0 until max) {
            //canvas_.drawPath(arrayPathList_.get(i), paint_);
            allPath_.addPath(arrayPathList_!![i]!!)
        }

        //setStrokeWidth(strokeWidth_);
        val strokeSize = pathMap_[nowPathNo_]!!.strokeWidth
        pathMap_[nowPathNo_] = DrawPathModel(strokeSize, allPath_)
    }

    @Synchronized
    private fun drawLines(reset: Boolean) {
        if (canvas_ == null) return
        if (reset) {
            allPath_ = Path()
            addPath()
            paint_.strokeWidth = pathMap_[nowPathNo_]!!.strokeWidth.toFloat()
            canvas_!!.drawPath(pathMap_[nowPathNo_]!!.allPath!!, paint_)
        } else {
            if (pathMap_[nowPathNo_]!!.allPath == null) {
                addPath()
            }
            for ((_, value) in pathMap_!!) {
                if (value.allPath != null) {
                    paint_.strokeWidth = value.strokeWidth.toFloat()
                    canvas_!!.drawPath(value.allPath!!, paint_)
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
//        val x = event.x
//        val y = event.y
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                singlePath_ = Path()
//                singlePath_!!.moveTo(x, y)
//                allPath_.moveTo(x, y)
//            }
//            MotionEvent.ACTION_MOVE -> {
//                singlePath_!!.lineTo(x, y)
//                allPath_.lineTo(x, y)
//            }
//            MotionEvent.ACTION_UP -> {
//                singlePath_!!.lineTo(x, y)
//                allPath_.lineTo(x, y)
//                arrayPathList_!!.add(singlePath_)
//            }
//        }
//        invalidate()
        return true
    }

    fun allDelete() {
        arrayPathList_ = ArrayList()
        //allPath_.reset();
        for ((_, value) in pathMap_) {
            if (value.allPath != null) value.allPath!!.reset()
        }
        invalidate()
    }

    fun oneBack() {
        if (arrayPathList_!!.size == 0) return
        arrayPathList_!!.removeAt(arrayPathList_!!.size - 1)
        if (pathMap_[nowPathNo_]!!.allPath == null) {
            pathMap_.remove(nowPathNo_)
            nowPathNo_ = nowPathNo_ - 1
            if (pathMap_[nowPathNo_]!!.allPath != null) {
                pathMap_[nowPathNo_]!!.allPath?.reset()
            }
        } else {
            pathMap_[nowPathNo_]!!.allPath!!.reset()
        }
        invalidate()
        drawLines(true)
    }

    companion object {
        //private int strokeWidth_ = 10;
        private const val STROKE_WIDTH_DEF = 10
    }

    init {
        pathMap_ = HashMap()
        allPath_ = Path()
        arrayPathList_ = ArrayList()
        paint_ = Paint()
        paint_.color = Color.RED
        paint_.style = Paint.Style.STROKE
        paint_.isAntiAlias = true
        //        paint_.setStrokeJoin(Paint.Join.ROUND);
//        paint_.setStrokeCap(Paint.Cap.ROUND);
        setStrokeWidth(strokeWidth_);
    }
}