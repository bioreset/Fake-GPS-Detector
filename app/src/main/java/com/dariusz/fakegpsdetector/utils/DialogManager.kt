package com.dariusz.fakegpsdetector.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.dariusz.fakegpsdetector.R
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
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

    fun showPermissionsNeededDialog(context: Context, action: () -> Unit): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle(R.string.permission_required_title)
            .setMessage(R.string.permission_required_body)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                action.invoke()
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
