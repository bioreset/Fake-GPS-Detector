package com.dariusz.fakegpsdetector.ui.firstscreen

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.model.LocationModel
import com.dariusz.fakegpsdetector.utils.DistanceCalculator.calculateDistance
import com.dariusz.fakegpsdetector.utils.DistanceCalculator.isRealLocation
import com.dariusz.fakegpsdetector.utils.Injectors
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.homescreen.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

class FirstScreenFragment : Fragment(R.layout.homescreen), OnMapReadyCallback {

    private val firstScreenViewModel: FirstScreenViewModel by viewModels {
        Injectors.provideFirstScreenViewModelFactory(requireContext())
    }

    private lateinit var mMap: GoogleMap

    private var resultx: String = ""

    @InternalCoroutinesApi
    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startLocationUpdate()
        (childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.getMapAsync(this)

        refresh_data.setOnClickListener {
            viewLifecycleOwner.lifecycle.coroutineScope.launch {
                doCheck()
                getStatus(checkStatusAfterAction())
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        firstScreenViewModel.locationData.observe(viewLifecycleOwner, Observer {
            viewLifecycleOwner.lifecycle.coroutineScope.launch {
                addToDb(it)
            }
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

    @SuppressLint("SetTextI18n")
    private suspend fun getStatus(result: String?) {
        if (result != null) {
            when (result) {
                "location" -> {
                    result_title.text = isTrueLocationString()
                }
                else -> {
                    result_title.text = "Error: Something went wrong"
                }
            }
        } else {
            result_title.text = "Empty database"
        }
    }

    private fun startLocationUpdate() {
        firstScreenViewModel.locationData.observe(viewLifecycleOwner, Observer {
            longitude_value.text = it.longitude.toString()
            latitude_value.text = it.latitude.toString()
        })
    }

    private suspend fun addToDb(location: LocationModel) {
        return firstScreenViewModel.repoLocation.insertAsFresh(location)
    }

    private suspend fun isTrueLocation(): Boolean {
        val calculator = calculateDistance(
            firstScreenViewModel.repoLocation.selectAll()!!.latitude,
            firstScreenViewModel.repoLocation.selectAll()!!.longitude,
            firstScreenViewModel.repoResult.selectAll().lat!!,
            firstScreenViewModel.repoResult.selectAll().lng!!
        )
        return isRealLocation(
            calculator,
            firstScreenViewModel.repoResult.selectAll().accuracy!!
        )
    }

    private suspend fun isTrueLocationString(): String {
        resultx = if (isTrueLocation()) {
            "True Location"
        } else {
            "Spoofed Location or too small sample size"
        }
        return resultx
    }

    private suspend fun doCheck() = firstScreenViewModel.performCheck()

    @InternalCoroutinesApi
    private suspend fun checkStatusAfterAction() =
        firstScreenViewModel.repoResult.checkLocationStatus()?.status
}
