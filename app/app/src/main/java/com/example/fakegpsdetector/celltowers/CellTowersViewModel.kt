package com.example.fakegpsdetector.celltowers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CellTowersViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Here will be cell towers list"
    }
    val text: LiveData<String> = _text
}