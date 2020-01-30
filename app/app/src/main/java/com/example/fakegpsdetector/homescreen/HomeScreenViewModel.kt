package com.example.fakegpsdetector.homescreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeScreenViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Here will be home screen"
    }
    val text: LiveData<String> = _text
}