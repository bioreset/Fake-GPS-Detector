package com.dariusz.fakegpsdetector.ui.firstscreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.ui.SharedViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.homescreen.*


class FirstScreenFragment : Fragment(), OnMapReadyCallback {

    private lateinit var locationData: FirstScreenViewModel

    private lateinit var systemData: SharedViewModel

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.homescreen, container, false)
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationData = ViewModelProvider(this).get(FirstScreenViewModel::class.java)
        systemData = ViewModelProvider(this).get(SharedViewModel::class.java)
        operateFirstFragment()
        (childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.getMapAsync(this)
    }

    private fun operateFirstFragment() {
        systemData.permissionCheck.observe(viewLifecycleOwner, Observer {
            updatePermissionCheckUI(it.status)
        })
        systemData.gpsStatus.observe(viewLifecycleOwner, Observer {
            updateGpsCheckUI(it.status)
        })
        systemData.wifiStatusCheck.observe(viewLifecycleOwner, Observer {
            updateWifiCheckUI(it.status)
        })
        startLocationUpdate()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        locationData.getLocationData().observe(viewLifecycleOwner, Observer {
            mMap.addMarker(
                MarkerOptions().position(LatLng(it.latitude, it.longitude))
                    .title("Your current location: ${it.latitude}, ${it.longitude} ")
            )
            mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(it.latitude, it.longitude),
                    18F
                )
            )
        })
    }

    fun updateGpsCheckUI(gStatus: Boolean) {
        when (gStatus) {
            true -> {
                gps_status.text =
                    getString(R.string.gps_status_enabled)
            }

            else -> {
                gps_status.text =
                    getString(R.string.gps_status_disabled)
            }
        }
    }

    fun updatePermissionCheckUI(pStatus: Boolean) {
        when (pStatus) {
            true -> {
                permissions_status?.text =
                    getString(R.string.permission_status_granted)

            }

            false -> {
                permissions_status?.text =
                    getString(R.string.permission_status_denied)
            }
        }
    }

    fun updateWifiCheckUI(wStatus: Boolean) {
        when (wStatus) {
            true -> {
                wifi_status?.text =
                    getString(R.string.wifi_status_enabled)

            }

            false -> {
                wifi_status?.text =
                    getString(R.string.wifi_status_disabled)
            }
        }
    }


    private fun startLocationUpdate() {
        locationData.getLocationData().observe(viewLifecycleOwner, Observer {
            longitude_value.text = it.longitude.toString()
            latitude_value.text = it.latitude.toString()
        })
    }

}
