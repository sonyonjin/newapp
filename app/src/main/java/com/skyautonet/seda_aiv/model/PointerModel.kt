package com.skyautonet.seda_aiv.model

import android.graphics.Point
import android.graphics.PointF
import android.widget.ImageView

data class PointerModel (
    val index: Int,
    val ivPinPointer: ImageView
) {
    val pointF: PointF = PointF(-1f, -1f)
    val touchPointF: PointF = PointF(-1f, -1f)
    val originalPoint: Point = Point(-1, -1)
    var scale: Float = 1f
    var layoutWidth: Int = 0
    var layoutHeight: Int = 0
    var actualImageWidth: Int = 0
    var actualImageHeight: Int = 0
    var widthMarginTotal: Int = 0
    var heightMarginTotal: Int = 0
    var halfWidthMargin = 0
    var halfHeightMargin = 0

    fun setSize(layoutWidth: Int, layoutHeight: Int, scale: Float, actualImageWidth: Int, actualImageHeight: Int) {
        this.layoutWidth = layoutWidth
        this.layoutHeight = layoutHeight
        this.scale = scale
        this.actualImageWidth = actualImageWidth
        this.actualImageHeight = actualImageHeight

        // layout사이즈중 가로변 세로변중 어느한쪽에 꽉차고 나머지 마진이 생긴경우 마진값만큼 범위에서 제거해야 포인트가 넘어가지않음
        if (layoutWidth != actualImageWidth) {
            widthMarginTotal = layoutWidth - actualImageWidth
        } else if (layoutHeight != actualImageHeight) {
            heightMarginTotal = layoutHeight - actualImageHeight
        }
        syncOriginalPointVariable()
    }

    fun setTouchPoint(x: Float, y: Float, isAdjust: Boolean) {

        // 화면비율과 원본이미지 비율이 맞지않아 마진이 생긴경우 마진값만큼 범위에서 제거해서 포인트가 넘어가지 않도록함
        // 이미지가 중간 배열이므로 위 아래 제한이 있어야됨
        if (widthMarginTotal > 0) {
            halfWidthMargin = widthMarginTotal / 2
        } else if (heightMarginTotal > 0) {
            halfHeightMargin = heightMarginTotal / 2
        }

        // 포인터 완쪽위 꼭지점 구하기위한 값
        // 터치좌표가 이미지 중간이어야 하니 시작포인트는 이미지크기의 절반만큼(동그라미 과년에 맞추기위해 약간조정) 앞을 잡아줘야됨

        val halfPointerWidthSize = ivPinPointer.width.toFloat() * 25 / 50
        val halfPointerHeightSize = ivPinPointer.height.toFloat() * 25 / 50

        // 미세조절시에는 가상의터치좌표를 대입하기위해 halfPointerSize를 더해준다
        var startPointX = x + (if (isAdjust) halfPointerWidthSize else 0f)
        var startPointY = y + (if (isAdjust) halfPointerHeightSize else 0f)

        if (startPointX < halfWidthMargin) {
            startPointX = halfWidthMargin.toFloat()
        } else if (startPointX > actualImageWidth + halfWidthMargin) {
            startPointX = (actualImageWidth + halfWidthMargin).toFloat()
        }
        if (startPointY < halfHeightMargin) {
            startPointY = halfHeightMargin.toFloat()
        } else if (startPointY > actualImageHeight + halfHeightMargin) {
            startPointY = (actualImageHeight + halfHeightMargin).toFloat()
        }

        // 미세조절시에는 가상의터치좌표를 대입하기위해 halfPointerSize를 더해준다
        touchPointF.set(startPointX, startPointY)

        ivPinPointer.setX(startPointX - halfPointerWidthSize)
        ivPinPointer.setY(startPointY - halfPointerHeightSize)
        syncOriginalPointVariable()
    }

    fun syncOriginalPointVariable() {
        pointF.set(ivPinPointer.x, ivPinPointer.y)

        val originalPointX = if (touchPointF.x - halfWidthMargin == 0f || scale == 0f) 0 else ((touchPointF.x - halfWidthMargin) / scale).toInt()
        val originalPointY = if (touchPointF.y - halfHeightMargin == 0f || scale == 0f) 0 else ((touchPointF.y - halfHeightMargin) / scale).toInt()
        originalPoint.set(originalPointX, originalPointY)
    }

//    fun translateTouchTo
}