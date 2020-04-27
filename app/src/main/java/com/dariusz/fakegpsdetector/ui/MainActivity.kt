package com.dariusz.fakegpsdetector.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.gps.GpsStatus
import com.dariusz.fakegpsdetector.permissions.PermissionStatus
import com.dariusz.fakegpsdetector.ui.firstscreen.FirstScreenFragment
import com.dariusz.fakegpsdetector.ui.secondscreen.SecondScreenFragment
import com.dariusz.fakegpsdetector.ui.thirdscreen.ThirdScreenFragment
import com.dariusz.fakegpsdetector.ui.thirdscreen.ThirdScreenViewModel
import com.dariusz.fakegpsdetector.wifistatus.WifiStatus
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainInterface {

    private var alertDialog: AlertDialog? = null

    private lateinit var mainViewModel : MainViewModel

    var gStatus: Boolean = false

    var pStatus: Boolean = false

    var wStatus: Boolean = false

    private val permStatus = Observer<PermissionStatus> {status ->
        status?.let {
            pStatus = when (status) {
                is PermissionStatus.Granted -> true
                is PermissionStatus.Denied -> false
            }
        }
    }

    private val gpsStatus = Observer<GpsStatus> {status ->
        status?.let {
            gStatus = when (status) {
                is GpsStatus.Enabled -> true
                is GpsStatus.Disabled -> false
            }
        }
    }

    private val wifiStatus = Observer<WifiStatus> {status ->
        status?.let {
            wStatus = when (status) {
                is WifiStatus.TurnedOn -> true
                is WifiStatus.TurnedOff -> false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nav_view.setOnNavigationItemSelectedListener(navListener())
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.container, FirstScreenFragment())
                .commit()
        }
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        checkStatuses()
        launchMain()
    }

    override fun onResume() {
        super.onResume()
        checkStatuses()
        launchMain()
    }

    private fun checkStatuses(){
        mainViewModel.doPermissionCheck().observe(this, permStatus)
        mainViewModel.getGpsStatus().observe(this, gpsStatus)
        mainViewModel.doWifiStatusCheck().observe(this, wifiStatus)
    }

    override fun returnpStatus() : Boolean {
        return pStatus
    }

    override fun returngStatus() : Boolean {
        return gStatus
    }

    override fun returnwStatus() : Boolean {
        return wStatus
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

    private fun launchMain() {
        when {
            !gStatus && pStatus -> {
                handleGpsAlertDialog()
            }
            gStatus && !pStatus -> {
                showPermissionsNeededDialog()
            }
            !gStatus && !pStatus && !wStatus -> {
                handleGpsAlertDialog()
                showPermissionsNeededDialog()
            }
        }
    }

    private fun handleGpsAlertDialog() {
        if (gStatus)
            hideGpsNotEnabledDialog()
        else
            showGpsNotEnabledDialog()
    }

    private fun showGpsNotEnabledDialog() {
        if (alertDialog?.isShowing == true) {
            return
        }

        alertDialog = AlertDialog.Builder(this)
            .setTitle(R.string.gps_required_title)
            .setMessage(R.string.gps_required_body)
            .setPositiveButton(R.string.action_settings) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
                this.startActivity(intent)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun hideGpsNotEnabledDialog() {
        if (alertDialog?.isShowing == true) alertDialog?.dismiss()
    }

    private fun showPermissionsNeededDialog() {
        if (alertDialog?.isShowing == true) {
            return
        }

        alertDialog = AlertDialog.Builder(this)
            .setTitle(R.string.permission_required_title)
            .setMessage(R.string.permission_required_body)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE
                    ),
                    1000
                )
            }
            .setCancelable(false)
            .create()

        alertDialog?.apply {
            show()
        }
    }

}