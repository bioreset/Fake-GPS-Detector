package com.dariusz.fakegpsdetector.ui

import androidx.fragment.app.Fragment

interface MainInterface {
    fun returnStatus(): String?
    fun doCheck()
    fun goToFragment(selectedFragment: Fragment)
}