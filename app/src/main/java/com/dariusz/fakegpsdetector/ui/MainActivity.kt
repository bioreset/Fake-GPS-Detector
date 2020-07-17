package com.dariusz.fakegpsdetector.ui

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.ui.firstscreen.FirstScreenFragment
import com.dariusz.fakegpsdetector.utils.DialogManager.dismissTheDialog
import com.dariusz.fakegpsdetector.utils.DialogManager.showGpsNotEnabledDialog
import com.dariusz.fakegpsdetector.utils.DialogManager.showPermissionsNeededDialog
import com.dariusz.fakegpsdetector.utils.DialogManager.showWifiAlertDialog
import com.dariusz.fakegpsdetector.utils.Injectors.provideSharedViewModelFactory
import com.dariusz.fakegpsdetector.utils.NavigationSetup.goToFragment
import com.dariusz.fakegpsdetector.utils.NavigationSetup.navListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: SharedViewModel by viewModels {
        provideSharedViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initNavigation()
        launchMain(this@MainActivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        turnOffMain(this@MainActivity)
    }

    private fun subscribeToPermissionCheck(context: Context) =
        mainViewModel.permissionCheck(
            context,
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
            )
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
        subscribeToPermissionCheck(context)
        subscribeToGpsStatus(context)
        subscribeToWifiStatus(context)
    }

    private fun initNavigation() {
        return supportFragmentManager.let {
            goToFragment(it, FirstScreenFragment())
            nav_view.setOnNavigationItemSelectedListener(navListener(it))
        }
    }

    private fun turnOffMain(context: Context) {
        mainViewModel.wifiStatusCheck(context).removeObservers(context as LifecycleOwner)
        mainViewModel.gpsStatus(context).removeObservers(context as LifecycleOwner)
        mainViewModel.permissionCheck(
            context,
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
            )
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
}
