package com.rebellionandroid.features.sectorslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SectorsListViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Sectors List View Model"
    }
    val text: LiveData<String> = _text
}