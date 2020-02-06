package com.example.fakegpsdetector

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fakegpsdetector.celltowers.CellTowersFragment
import com.example.fakegpsdetector.homescreen.HomeScreenFragment
import com.example.fakegpsdetector.location.GpsStatus
import com.example.fakegpsdetector.location.LocationService.Companion.isServiceRunning
import com.example.fakegpsdetector.location.LocationService.Companion.isTrackingRunning
import com.example.fakegpsdetector.location.PermissionStatus
import com.example.fakegpsdetector.routers.RoutersFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import kotlinx.android.synthetic.main.homescreen.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private var alertDialog: AlertDialog? = null

    private val gpsObserver = Observer<GpsStatus> { status ->
        status?.let {
            updateGpsCheckUI(status)
        }
    }

    private val permissionObserver = Observer<PermissionStatus> { status ->
        status?.let {
            updatePermissionCheckUI(status)
            when (status) {
                is PermissionStatus.Granted -> handleGpsAlertDialog()
                is PermissionStatus.Denied -> showLocationPermissionNeededDialog()
            }
        }
    }

    private val permissionHandler = object : PermissionHandler() {
        override fun onGranted() {
            updatePermissionCheckUI(PermissionStatus.Granted())
            handleGpsAlertDialog()
        }

        override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
            updatePermissionCheckUI(PermissionStatus.Denied())
        }

    }

    private fun updateGpsCheckUI(status: GpsStatus) {
        when (status) {
            is GpsStatus.Enabled -> {
                gpsStatusDisplay.isEnabled = false
                gpsStatusDisplay.apply {
                    text = getString(status.message)
                    setTextColor(Color.BLUE)
                }

                handleGpsAlertDialog(GpsStatus.Enabled())
            }

            is GpsStatus.Disabled -> {
                gpsStatusDisplay.isEnabled = true
                gpsStatusDisplay.apply {
                    text = getString(status.message).plus(getString(R.string.click_to_retry))
                    setTextColor(Color.RED)
                }
            }
        }

    }

    private fun updatePermissionCheckUI(status: PermissionStatus) {
        when (status) {
            is PermissionStatus.Granted -> {
                permissionStatusDisplay.isEnabled = false
                permissionStatusDisplay.apply {
                    text = getString(status.message)
                    setTextColor(Color.BLUE)
                }
            }

            is PermissionStatus.Denied -> {
                permissionStatusDisplay.isEnabled = true
                permissionStatusDisplay.apply {
                    text = getString(status.message).plus(getString(R.string.click_to_retry))
                    setTextColor(Color.RED)
                }
            }

        }

    }

    private fun isTrackingRunningAlready() = isTrackingRunning && isServiceRunning

    private fun setupUI() {

        if (isTrackingRunningAlready().not())
            startTracking()
        else
            stopTracking()


        Handler().apply {
            postDelayed({ viewModel.startLocationTracking() }, 3000)
        }


        gpsStatusDisplay.setOnClickListener { handleGpsAlertDialog() }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionStatusDisplay.visibility = View.VISIBLE
            permissionStatusDisplay.setOnClickListener { showLocationPermissionNeededDialog() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.nav_view)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val firstFragment = HomeScreenFragment()
        openFragment(firstFragment)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        subscribeToGpsListener()
        subscribeToLocationPermissionListener()
        startLocationUpdate()
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.homescreen -> {
                    val firstFragment = HomeScreenFragment()
                    openFragment(firstFragment)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.routers -> {
                    val secondFragment = RoutersFragment()
                    openFragment(secondFragment)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.celltowers -> {
                    val thirdFragment = CellTowersFragment()
                    openFragment(thirdFragment)
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


    private fun subscribeToGpsListener() = viewModel.gpsStatusLiveData
        .observe(this, gpsObserver)

    private fun subscribeToLocationPermissionListener() =
        viewModel.locationPermissionStatusLiveData.observe(this, permissionObserver)

    private fun startTracking() {
        viewModel.startLocationTracking()
    }

    private fun stopTracking() {
        viewModel.stopLocationTracking()
    }

    override fun onResume() {
        super.onResume()
        setupUI()
    }

    private fun handleGpsAlertDialog(status: GpsStatus = viewModel.gpsStatusLiveData.value as GpsStatus) {
        when (status) {
            is GpsStatus.Enabled -> hideGpsNotEnabledDialog()
            is GpsStatus.Disabled -> showGpsNotEnabledDialog()
        }
    }

    private fun showGpsNotEnabledDialog() {
        if (alertDialog?.isShowing == true) {
            return // null or already being shown
        }

        alertDialog = AlertDialog.Builder(this)
            .setTitle(R.string.gps_required_title)
            .setMessage(R.string.gps_required_body)
            .setPositiveButton(R.string.action_settings) { _, _ ->
                // Open app's settings.
                val intent = Intent().apply {
                    action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
                }
                startActivity(intent)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun hideGpsNotEnabledDialog() {
        if (alertDialog?.isShowing == true) alertDialog?.dismiss()
    }

    private fun showLocationPermissionNeededDialog() {
        if (alertDialog?.isShowing == true) {
            return // null or dialog already being shown
        }

        alertDialog = AlertDialog.Builder(this)
            .setTitle(R.string.permission_required_title)
            .setMessage(R.string.permission_required_body)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                //Using 3rd party lib *Permissions* for showing dialogs and handling response
                Permissions.check(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    null,
                    permissionHandler
                )
            }
            .setCancelable(false) //to disable outside click for cancel
            .create()

        alertDialog?.apply {
            show()
        }
    }

    private fun startLocationUpdate() {
        viewModel.getLocationData().observe(this, Observer {
            longitude_value.text = it.longitude.toString()
            latitude_value.text = it.latitude.toString()
        })
    }
}

