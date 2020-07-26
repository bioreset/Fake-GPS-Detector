package com.dariusz.fakegpsdetector.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.dariusz.fakegpsdetector.databinding.ActivityMainBinding
import com.dariusz.fakegpsdetector.ui.firstscreen.FirstScreenFragment
import com.dariusz.fakegpsdetector.utils.DialogManager.dismissTheDialog
import com.dariusz.fakegpsdetector.utils.DialogManager.showGpsNotEnabledDialog
import com.dariusz.fakegpsdetector.utils.DialogManager.showPermissionsNeededDialog
import com.dariusz.fakegpsdetector.utils.DialogManager.showWifiAlertDialog
import com.dariusz.fakegpsdetector.utils.Injectors.provideSharedViewModelFactory
import com.dariusz.fakegpsdetector.utils.NavigationSetup.goToFragment
import com.dariusz.fakegpsdetector.utils.NavigationSetup.navListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: SharedViewModel by viewModels {
        provideSharedViewModelFactory()
    }

    private var permissionArray: List<String> = ArrayList()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setPermissionArray()
        launchMain(this@MainActivity)
        mainContext = this@MainActivity
    }

    override fun onDestroy() {
        super.onDestroy()
        turnOffMain(this@MainActivity)
    }

    private fun setPermissionArray() {
        permissionArray = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE
        )
    }

    private fun subscribeToPermissionCheck(context: Context) =
        mainViewModel.permissionCheck(
            context,
            permissionArray
        ).observe(
            context as LifecycleOwner,
            Observer {
                handleAlertPermissions(context, it.status)
            }
        )

    private fun subscribeToGpsStatus(context: Context) =
        mainViewModel.gpsStatus(context).observe(
            context as LifecycleOwner,
            Observer {
                handleAlertGps(context, it.status)
            }
        )

    private fun subscribeToWifiStatus(context: Context) =
        mainViewModel.wifiStatusCheck(context).observe(
            context as LifecycleOwner,
            Observer {
                handleAlertWifi(context, it.status)
            }
        )

    private fun launchMain(context: Context) {
        initNavigation()
        subscribeToPermissionCheck(context)
        subscribeToGpsStatus(context)
        subscribeToWifiStatus(context)
    }

    private fun initNavigation() {
        return supportFragmentManager.let {
            goToFragment(it, FirstScreenFragment())
            binding.navView.setOnNavigationItemSelectedListener(navListener(it))
        }
    }

    private fun turnOffMain(context: Context) {
        mainViewModel.wifiStatusCheck(context).removeObservers(context as LifecycleOwner)
        mainViewModel.gpsStatus(context).removeObservers(context as LifecycleOwner)
        mainViewModel.permissionCheck(
            context,
            permissionArray
        ).removeObservers(context as LifecycleOwner)
    }

    private fun handleAlertPermissions(context: Context, status: Boolean) {
        when (status) {
            false -> showPermissionsNeededDialog(context)
            true -> dismissTheDialog(showPermissionsNeededDialog(context))
        }
    }

    private fun handleAlertGps(context: Context, status: Boolean) {
        when (status) {
            false -> showGpsNotEnabledDialog(context)
            true -> dismissTheDialog(showGpsNotEnabledDialog(context))
        }
    }

    private fun handleAlertWifi(context: Context, status: Boolean) {
        when (status) {
            false -> showWifiAlertDialog(context)
            true -> dismissTheDialog(showWifiAlertDialog(context))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1000) {
            if (grantResults.isNotEmpty() && grantResults.all {
                it == PackageManager.PERMISSION_GRANTED
            }
            )
                launchMain(this@MainActivity)
            else {
                turnOffMain(this@MainActivity)
            }
        }
    }

    companion object {
        var mainContext: Context? = null
            private set
    }
}
