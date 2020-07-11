package com.dariusz.fakegpsdetector.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.ui.firstscreen.FirstScreenFragment
import com.dariusz.fakegpsdetector.ui.secondscreen.SecondScreenFragment
import com.dariusz.fakegpsdetector.ui.thirdscreen.ThirdScreenFragment
import com.dariusz.fakegpsdetector.utils.DialogManager.dismissTheDialog
import com.dariusz.fakegpsdetector.utils.DialogManager.showGpsNotEnabledDialog
import com.dariusz.fakegpsdetector.utils.DialogManager.showPermissionsNeededDialog
import com.dariusz.fakegpsdetector.utils.DialogManager.showWifiAlertDialog
import com.dariusz.fakegpsdetector.utils.Injectors
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mainViewModel: SharedViewModel by viewModels {
        Injectors.provideSharedViewModelFactory(this)
    }

    private lateinit var firstScreenFragment: FirstScreenFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstScreenFragment = FirstScreenFragment()

        goToFragment(firstScreenFragment)

        nav_view.setOnNavigationItemSelectedListener(navListener())

        launchMain()

    }

    override fun onDestroy() {
        super.onDestroy()
        turnOffMain()
    }

    private fun subscribeToPermissionCheck() =
        mainViewModel.permissionCheck.observe(this, Observer {
            handleAlertPermissions(it.status)
        })

    private fun subscribeToGpsStatus() =
        mainViewModel.gpsStatus.observe(this, Observer {
            handleAlertGps(it.status)
        })

    private fun subscribeToWifiStatus() =
        mainViewModel.wifiStatusCheck.observe(this, Observer {
            handleAlertWifi(it.status)
        })

    private fun launchMain() {
        subscribeToPermissionCheck()
        subscribeToGpsStatus()
        subscribeToWifiStatus()
    }

    private fun turnOffMain() {
        mainViewModel.wifiStatusCheck.removeObservers(this@MainActivity)
        mainViewModel.gpsStatus.removeObservers(this@MainActivity)
        mainViewModel.permissionCheck.removeObservers(this@MainActivity)
    }

    private fun goToFragment(selectedFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, selectedFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun navListener(): BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            val selectedFragment: Fragment
            when (menuItem.itemId) {
                R.id.navigation_homescreen -> {
                    selectedFragment = FirstScreenFragment()
                    goToFragment(selectedFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_routers -> {
                    selectedFragment = SecondScreenFragment()
                    goToFragment(selectedFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_celltowers -> {
                    selectedFragment = ThirdScreenFragment()
                    goToFragment(selectedFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }


    private fun handleAlertPermissions(status: Boolean) {
        when (status) {
            false -> showPermissionsNeededDialog(this@MainActivity)
            true -> dismissTheDialog(showPermissionsNeededDialog(this@MainActivity))
        }
    }

    private fun handleAlertGps(status: Boolean) {
        when (status) {
            false -> showGpsNotEnabledDialog(this@MainActivity)
            true -> dismissTheDialog(showGpsNotEnabledDialog(this@MainActivity))
        }
    }

    private fun handleAlertWifi(status: Boolean) {
        when (status) {
            false -> showWifiAlertDialog(this@MainActivity)
            true -> dismissTheDialog(showWifiAlertDialog(this@MainActivity))
        }
    }


}