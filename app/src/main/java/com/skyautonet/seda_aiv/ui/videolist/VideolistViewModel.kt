package com.skyautonet.seda_aiv.ui.videolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VideolistViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is video list Fragment"
    }
    val text: LiveData<String> = _text
}