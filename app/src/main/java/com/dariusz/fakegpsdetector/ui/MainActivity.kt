package com.dariusz.fakegpsdetector.ui

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.databinding.ActivityMainBinding
import com.dariusz.fakegpsdetector.utils.DialogManager.dismissTheDialog
import com.dariusz.fakegpsdetector.utils.DialogManager.showGpsNotEnabledDialog
import com.dariusz.fakegpsdetector.utils.DialogManager.showPermissionsNeededDialog
import com.dariusz.fakegpsdetector.utils.DialogManager.showWifiAlertDialog
import com.dariusz.fakegpsdetector.utils.Injectors.provideSharedViewModelFactory
import com.dariusz.fakegpsdetector.utils.NavigationSetup.setupWithNavController
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

    private var currentNavController: LiveData<NavController>? = null

    private var savedState: Bundle? = null

    private val askMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            currentNavController?.value?.navigate(R.id.fragment_first_screen)
            if (savedState == null) {
                setupBottomNavigationBar()
            }
            launchMain(this@MainActivity)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        savedState = savedInstanceState
        val view = binding.root
        setContentView(view)
        setPermissionArray()
        if (savedState == null) {
            setupBottomNavigationBar()
        }
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
            {
                handleAlertPermissions(it.status)
            }
        )

    private fun subscribeToGpsStatus(context: Context) =
        mainViewModel.gpsStatus(context).observe(
            context as LifecycleOwner,
            {
                handleAlertGps(context, it.status)
            }
        )

    private fun subscribeToWifiStatus(context: Context) =
        mainViewModel.wifiStatusCheck(context).observe(
            context as LifecycleOwner,
            {
                handleAlertWifi(context, it.status)
            }
        )

    private fun launchMain(context: Context) {
        subscribeToPermissionCheck(context)
        subscribeToGpsStatus(context)
        subscribeToWifiStatus(context)
    }

    private fun turnOffMain(context: Context) {
        mainViewModel.wifiStatusCheck(context).removeObservers(context as LifecycleOwner)
        mainViewModel.gpsStatus(context).removeObservers(context as LifecycleOwner)
        mainViewModel.permissionCheck(
            context,
            permissionArray
        ).removeObservers(context as LifecycleOwner)
    }

    private fun setupBottomNavigationBar() {
        val bottomNav = binding.navView
        val controller = bottomNav.setupWithNavController(
            navGraphIds = listOf(
                R.navigation.bottom_navigation_first_screen,
                R.navigation.bottom_navigation_second_screen,
                R.navigation.bottom_navigation_third_screen
            ),
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
            intent = intent
        )
        controller.observe(
            this@MainActivity,
            {
                setupActionBarWithNavController(it)
            }
        )
        currentNavController = controller
    }

    private fun handleAlertPermissions(status: Boolean) {
        when (status) {
            false -> permissionAlert()
            true -> dismissTheDialog(permissionAlert())
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

    private fun permissionAlert() =
        showPermissionsNeededDialog(this@MainActivity) { invokePermissionAction() }

    private fun invokePermissionAction() {
        askMultiplePermissions.launch(permissionArray.toTypedArray())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    companion object {
        var mainContext: Context? = null
            private set
    }
}
