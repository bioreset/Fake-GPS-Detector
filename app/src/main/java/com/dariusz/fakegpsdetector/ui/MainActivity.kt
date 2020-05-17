package com.dariusz.fakegpsdetector.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.api.CreateJSONRequest
import com.dariusz.fakegpsdetector.api.retrofit.RestApiService
import com.dariusz.fakegpsdetector.repository.LocationFromApiResponseRepository
import com.dariusz.fakegpsdetector.ui.firstscreen.FirstScreenFragment
import com.dariusz.fakegpsdetector.ui.secondscreen.SecondScreenFragment
import com.dariusz.fakegpsdetector.ui.thirdscreen.ThirdScreenFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainInterface {

    private var alertDialog: AlertDialog? = null

    private lateinit var mainViewModel: SharedViewModel

    private lateinit var api: RestApiService

    private lateinit var firstScreenFragment: FirstScreenFragment

    private lateinit var result: LocationFromApiResponseRepository

    private var status: String? = "error"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nav_view.setOnNavigationItemSelectedListener(navListener())
        goToFragment(FirstScreenFragment())

        mainViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        launchMain()

        firstScreenFragment = FirstScreenFragment()

        api = RestApiService(this)

        result = LocationFromApiResponseRepository.getInstance(this)

        refresh_data.setOnClickListener {
            checkLocation(createJson())
           // status = result.selectAll().status
        }

    }

    private fun createJson() = CreateJSONRequest(this).buildJSONRequest()

    private fun checkLocation(json: String) {
        return api.checkLocation(json)
    }

    override fun returnStatus(): String? {
        return status
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


    private fun handleAlertGps(status: Boolean) {
        when (status) {
            false -> showGpsNotEnabledDialog()
        }
    }

    private fun handleAlertPermissions(status: Boolean) {
        when (status) {
            false -> showPermissionsNeededDialog()
        }
    }
    private fun handleAlertWifi(status: Boolean) {
        when (status) {
            false -> showWifiAlertDialog()
        }
    }

    private fun showGpsNotEnabledDialog() {
        alertDialog = AlertDialog.Builder(this)
                .setTitle(R.string.gps_required_title)
                .setMessage(R.string.gps_required_body)
                .setPositiveButton(R.string.action_settings) { _, _ ->
                    val intent = Intent()
                    intent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    this.startActivity(intent)
                }
                .setNegativeButton(android.R.string.cancel, null)
                .create()

        alertDialog?.apply {
            show()
        }
    }

    private fun showPermissionsNeededDialog() {
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
                                    Manifest.permission.CHANGE_WIFI_STATE,
                                    Manifest.permission.READ_PHONE_STATE
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

    private fun showWifiAlertDialog() {
        alertDialog = AlertDialog.Builder(this)
                .setTitle(R.string.wifi_required_title)
                .setMessage(R.string.wifi_required_body)
                .setPositiveButton(R.string.action_settings) { _, _ ->
                    val intent = Intent()
                    intent.action = Settings.ACTION_WIFI_SETTINGS
                    this.startActivity(intent)
                }
                .setNegativeButton(android.R.string.cancel, null)
                .create()

        alertDialog?.apply {
            show()
        }
    }

}