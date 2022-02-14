package com.skyautonet.seda_aiv.ui.videostorage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VideoStorageViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is video storage Fragment"
    }
    val text: LiveData<String> = _text
}