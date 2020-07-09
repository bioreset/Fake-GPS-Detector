package com.dariusz.fakegpsdetector.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.dariusz.fakegpsdetector.R

object DialogManager {

    fun showGpsNotEnabledDialog(context: Context): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle(R.string.gps_required_title)
            .setMessage(R.string.gps_required_body)
            .setPositiveButton(R.string.action_settings) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
                context.startActivity(intent)
            }
            .setCancelable(false)
            .create()
            .apply { show() }
    }

    fun showPermissionsNeededDialog(context: Context): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle(R.string.permission_required_title)
            .setMessage(R.string.permission_required_body)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                ActivityCompat.requestPermissions(
                    context as Activity,
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
            .apply {
                show()
            }
    }

    fun showWifiAlertDialog(context: Context): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle(R.string.wifi_required_title)
            .setMessage(R.string.wifi_required_body)
            .setPositiveButton(R.string.action_settings) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_WIFI_SETTINGS
                context.startActivity(intent)
            }
            .setCancelable(false)
            .create()
            .apply {
                show()
            }
    }

    fun dismissTheDialog(dialog: AlertDialog) = dialog.dismiss()

}