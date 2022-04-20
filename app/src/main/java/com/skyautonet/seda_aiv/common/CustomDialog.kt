package com.skyautonet.seda_aiv.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import com.skyautonet.seda_aiv.common.CustomDialog.IOnClickItemListener
import android.widget.TextView
import android.os.Bundle
import com.skyautonet.seda_aiv.R
import android.view.WindowManager
import android.view.Gravity
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.widget.ImageView

class CustomDialog(
    context: Context?,
    private val mTitle: String?,
    private val mImage: Int,
    private val mTitleButton: String?,
    private val mTvCancel: String?,
    private val mListener: IOnClickItemListener?
) : Dialog(
    context!!) {

    private lateinit var mTvTitle: TextView
    private lateinit var mIvIcon: ImageView
    private lateinit var mBtnOK: TextView
    private lateinit var mBtnCancel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_custom)
        val window = this.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND
        window.attributes = wlp
        getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.attributes = wlp
        initComponents()
        addListener()
    }

    private fun initComponents() {
        mTvTitle = findViewById(R.id.tv_title)
        mIvIcon = findViewById(R.id.iv_icon)
        mBtnOK = findViewById(R.id.tv_btn_ok)
        mBtnCancel = findViewById(R.id.tv_btn_cancel)
        if (!TextUtils.isEmpty(mTitle)) {
            mTvTitle.setText(mTitle)
        }
        if (mImage > 0) {
            mIvIcon.setImageResource(mImage)
        } else {
            mIvIcon.setVisibility(View.GONE)
        }
        if (!TextUtils.isEmpty(mTitleButton)) {
            mBtnOK.setText(mTitleButton)
        }
        if (!TextUtils.isEmpty(mTvCancel)) {
            mBtnCancel.setVisibility(View.VISIBLE)
            mBtnCancel.setText(mTvCancel)
        } else {
            mBtnCancel.setVisibility(View.GONE)
        }
    }

    private fun addListener() {
        mBtnOK.setOnClickListener { v: View? -> mListener?.onClickConfirm(this) }
        if (mBtnCancel.visibility == View.VISIBLE) {
            mBtnCancel.setOnClickListener { v: View? -> dismiss() }
        }
    }

    interface IOnClickItemListener {
        fun onClickConfirm(dialog: Dialog)
    }
}