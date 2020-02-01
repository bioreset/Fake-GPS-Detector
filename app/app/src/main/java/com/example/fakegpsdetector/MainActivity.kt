package com.example.fakegpsdetector

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fakegpsdetector.celltowers.CellTowersFragment
import com.example.fakegpsdetector.homescreen.HomeScreenFragment
import com.example.fakegpsdetector.mapview.MapViewFragment
import com.example.fakegpsdetector.routers.RoutersFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.nav_view)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val firstFragment = HomeScreenFragment()
        openFragment(firstFragment)

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {

            R.id.homescreen -> {
                val firstFragment = HomeScreenFragment()
                openFragment(firstFragment)
                return@OnNavigationItemSelectedListener true
            }

            R.id.mapview -> {
                val secondFragment = MapViewFragment()
                openFragment(secondFragment)
                return@OnNavigationItemSelectedListener true
            }

            R.id.routers -> {
                val thirdFragment = RoutersFragment()
                openFragment(thirdFragment)
                return@OnNavigationItemSelectedListener true
            }

            R.id.celltowers -> {
                val fourthFragment = CellTowersFragment()
                openFragment(fourthFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
