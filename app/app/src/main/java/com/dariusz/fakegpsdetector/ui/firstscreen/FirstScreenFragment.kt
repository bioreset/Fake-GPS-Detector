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
import com.dariusz.fakegpsdetector.api.apimodel.ApiResponseModel
import com.dariusz.fakegpsdetector.model.LocationModel
import com.dariusz.fakegpsdetector.repository.LocationFromApiResponseRepository
import com.dariusz.fakegpsdetector.repository.LocationRepository
import com.dariusz.fakegpsdetector.ui.SharedViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.homescreen.*

class FirstScreenFragment : Fragment(), OnMapReadyCallback {

    private lateinit var firstScreenViewModel: FirstScreenViewModel

    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var mMap: GoogleMap

    private lateinit var repo_location: LocationRepository

    private lateinit var repo_result: LocationFromApiResponseRepository

    override fun onAttach(context: Context) {
        super.onAttach(context)
        repo_location = LocationRepository.getInstance(context)
        repo_result = LocationFromApiResponseRepository.getInstance(context)
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
        firstScreenViewModel = ViewModelProvider(this).get(FirstScreenViewModel::class.java)
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        operateFirstFragment()
        (childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.getMapAsync(this)
    }

    private fun operateFirstFragment() {
        sharedViewModel.permissionCheck.observe(viewLifecycleOwner, Observer {
            updatePermissionCheckUI(it.status)
        })
        sharedViewModel.gpsStatus.observe(viewLifecycleOwner, Observer {
            updateGpsCheckUI(it.status)
        })
        sharedViewModel.wifiStatusCheck.observe(viewLifecycleOwner, Observer {
            updateWifiCheckUI(it.status)
        })
        startLocationUpdate()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        firstScreenViewModel.getLocationData().observe(viewLifecycleOwner, Observer {
            addToDb(it)
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

    private fun updateGpsCheckUI(gStatus: Boolean) {
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

    private fun updatePermissionCheckUI(pStatus: Boolean) {
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

    private fun updateWifiCheckUI(wStatus: Boolean) {
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

    fun getStatus() {
        when (showResult().status) {
            "location" -> {
                result_title.text = showResult().status
                result_lat.text = showResult().lat.toString()
                result_long.text = showResult().long.toString()
                result_accuracy.text = showResult().accuracy.toString()
            }
            "error" -> {
                result_title.text = "Error: Something went wrong"
                result_lat.visibility = View.GONE
                result_long.visibility = View.GONE
                result_accuracy.visibility = View.GONE
            }
        }
    }

    private fun startLocationUpdate() {
        firstScreenViewModel.getLocationData().observe(viewLifecycleOwner, Observer {
            longitude_value.text = it.longitude.toString()
            latitude_value.text = it.latitude.toString()
        })
    }

    private fun addToDb(location: LocationModel) {
        return repo_location.insert(location)
    }

    private fun showResult(): ApiResponseModel {
        return repo_result.selectAll()
    }

}
