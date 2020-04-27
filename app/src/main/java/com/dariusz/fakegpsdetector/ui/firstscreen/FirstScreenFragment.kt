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
import com.dariusz.fakegpsdetector.ui.MainInterface
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.homescreen.*


class FirstScreenFragment : Fragment(), OnMapReadyCallback {

    private lateinit var locationData: FirstScreenViewModel

    private lateinit var listenerFromMA: MainInterface

    private lateinit var mMap: GoogleMap

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listenerFromMA = context as MainInterface
    }

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
        (childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.getMapAsync(this)
        locationData = ViewModelProvider(this).get(FirstScreenViewModel::class.java)
        operateFirstFragment()
    }

    private fun operateFirstFragment() {
        updateGpsCheckUI()
        updatePermissionCheckUI()
        startLocationUpdate()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        locationData.getLocationData().observe(viewLifecycleOwner, Observer {
            mMap.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)).title("Your current location"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 18F))
        })

    }

    private fun updateGpsCheckUI() {
        when (listenerFromMA.returngStatus()) {
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

    private fun updatePermissionCheckUI() {
        when (listenerFromMA.returnpStatus()) {
            true -> {
                permissions_status?.text =
                    getString(R.string.permission_status_granted)

            }

            else -> {
                permissions_status?.text =
                    getString(R.string.permission_status_denied)
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
