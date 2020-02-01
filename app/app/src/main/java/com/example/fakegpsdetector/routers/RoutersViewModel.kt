package com.example.fakegpsdetector.routers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RoutersViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Here will be routers list"
    }
    val text: LiveData<String> = _text
}