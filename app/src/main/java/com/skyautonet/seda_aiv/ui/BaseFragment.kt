package com.skyautonet.seda_aiv.ui

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment

import com.google.gson.Gson
import com.skyautonet.seda_aiv.SAApp
import com.skyautonet.seda_aiv.common.CommonUtils
import com.skyautonet.seda_aiv.common.SharedPref
import com.skyautonet.seda_aiv.rest.SAAppInterface


import retrofit2.Retrofit
import javax.inject.Inject

open class BaseFragment : Fragment(), BaseUseCase {

    lateinit var dialog: Dialog

    @Inject
    lateinit var saAppInterface: SAAppInterface

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var commonUtils: CommonUtils

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as SAApp).getNetComponent().inject(this)
        dialog = commonUtils.createCustomLoader(requireContext(), false)
    }

    override fun showLoader() {
        dialog.show()
    }

    override fun hideLoader() {
        dialog.dismiss()
    }

    override fun onBackPress() {
    }
}