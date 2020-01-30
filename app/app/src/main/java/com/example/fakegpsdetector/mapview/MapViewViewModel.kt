package com.example.fakegpsdetector.mapview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Here will be map view"
    }
    val text: LiveData<String> = _text
}