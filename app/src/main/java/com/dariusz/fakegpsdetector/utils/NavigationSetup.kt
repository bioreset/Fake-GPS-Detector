package com.dariusz.fakegpsdetector.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.ui.firstscreen.FirstScreenFragment
import com.dariusz.fakegpsdetector.ui.secondscreen.SecondScreenFragment
import com.dariusz.fakegpsdetector.ui.thirdscreen.ThirdScreenFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
object NavigationSetup {

    fun navListener(fragmentManager: FragmentManager) =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_homescreen -> {
                    goToFragment(fragmentManager, FirstScreenFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_routers -> {
                    goToFragment(fragmentManager, SecondScreenFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_celltowers -> {
                    goToFragment(fragmentManager, ThirdScreenFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    fun goToFragment(fragmentManager: FragmentManager, selectedFragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.container, selectedFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
