package com.example.fakegpsdetector.homescreen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class HomeScreenViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Here will be routers list"
    }
    val text: LiveData<String> = _text

}