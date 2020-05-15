package com.dariusz.fakegpsdetector.phoneinfo

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.model.PhoneInfoModel
import com.dariusz.fakegpsdetector.repository.PhoneInfoRepository

class PhoneInfoService : Service() {

    private var radiotype: String = ""

    private var mccmnc: String = ""

    private var carriername = ""

    private lateinit var result: List<String>

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        val telephonyManager: TelephonyManager? = applicationContext?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
        radiotype = telephonyManager?.dataNetworkType.toString()
        mccmnc = telephonyManager?.networkOperator ?: "hi operator"
        carriername = telephonyManager?.networkOperatorName ?: "operator name hi"

        prepareData()
        newInfoList()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        addToDb()
        var builder = NotificationCompat.Builder(this, createNotificationChannel("qwe", "asd"))
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_light)
                .setContentTitle("textTitle")
                .setContentText("textContent")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        startForeground(2, builder.build())
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun prepareData(): List<String> {
        result = mutableListOf(radiotype, mccmnc, carriername)
        Log.d("QWE", "asd : $result")
        return result
    }

    private fun newInfoList(): PhoneInfoModel {
        return PhoneInfoModel(
                homeMobileCountryCode = prepareData()[1].substring(0, 2),
                homeMobileNetworkCode = prepareData()[1].substring(3, 5),
                radioType = prepareData()[0],
                carrier = prepareData()[2]
        )
    }

    private fun addToDb() {
        return PhoneInfoRepository.getInstance(this.applicationContext).insert(newInfoList())
    }

}