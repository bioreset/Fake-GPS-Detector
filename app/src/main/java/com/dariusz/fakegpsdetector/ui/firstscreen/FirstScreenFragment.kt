package com.dariusz.fakegpsdetector.ui.firstscreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.model.LocationModel
import com.dariusz.fakegpsdetector.utils.DistanceCalculator.calculateDistance
import com.dariusz.fakegpsdetector.utils.DistanceCalculator.isRealLocation
import com.dariusz.fakegpsdetector.utils.Injectors.provideFirstScreenViewModelFactory
import com.dariusz.fakegpsdetector.utils.ViewUtils.performActionInsideCoroutine
import com.dariusz.fakegpsdetector.utils.ViewUtils.performActionInsideCoroutineWithLiveData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.homescreen.*
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class FirstScreenFragment : Fragment(R.layout.homescreen), OnMapReadyCallback {

    private val firstScreenViewModel: FirstScreenViewModel by viewModels {
        provideFirstScreenViewModelFactory(requireContext())
    }

    private lateinit var googleMapObject: GoogleMap

    @InternalCoroutinesApi
    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startLocationUpdate()
        (childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.getMapAsync(this)

        refresh_data.setOnClickListener {
            performActionInsideCoroutine(viewLifecycleOwner) {
                performCheck()
                getStatus(checkStatusAfterAction())
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMapObject = googleMap!!

        performActionInsideCoroutineWithLiveData(
            fetchLocationData(),
            viewLifecycleOwner,
            actionInCoroutine = {
                addToDb(it!!)
            },
            actionOnMain = {
                it!!
                googleMapObject.addMarker(
                    MarkerOptions().position(LatLng(it.latitude, it.longitude))
                        .title("Your current location: ${it.latitude}, ${it.longitude} ")
                ).remove()
                googleMapObject.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(it.latitude, it.longitude),
                        18F
                    )
                )
            }
        )
    }

    private fun fetchLocationData() = firstScreenViewModel.locationData(requireContext())

    private fun repoLocationConnection() = firstScreenViewModel.repoLocation

    private fun repoResultConnection() = firstScreenViewModel.repoResult

    private suspend fun performCheck() = repoResultConnection().performAction(requireContext())

    private suspend fun getStatus(result: String?) {
        if (result != null) {
            when (result) {
                "location" -> {
                    result_title.text = isTrueLocationString()
                }
                else -> {
                    result_title.text = getString(R.string.error_checking_location_text)
                }
            }
        } else {
            result_title.text = getString(R.string.empty_db_text)
        }
    }

    private fun startLocationUpdate() {
        fetchLocationData().observe(
            viewLifecycleOwner,
            Observer {
                longitude_value.text = it.longitude.toString()
                latitude_value.text = it.latitude.toString()
            }
        )
    }

    private suspend fun addToDb(location: LocationModel) {
        return repoLocationConnection().insertAsFresh(location)
    }

    private suspend fun isTrueLocation(): Boolean {
        val calculator = calculateDistance(
            repoLocationConnection().selectAll()!!.latitude,
            repoLocationConnection().selectAll()!!.longitude,
            repoResultConnection().selectAll().lat!!,
            repoResultConnection().selectAll().lng!!
        )
        return isRealLocation(
            calculator,
            repoResultConnection().selectAll().accuracy!!
        )
    }

    private suspend fun isTrueLocationString(): String {
        return if (isTrueLocation()) {
            getString(R.string.true_location_text)
        } else {
            getString(R.string.spoofed_location_text)
        }
    }

    @InternalCoroutinesApi
    private suspend fun checkStatusAfterAction() =
        repoResultConnection().checkLocationStatus()?.status

    override fun onDestroyView() {
        super.onDestroyView()
        fetchLocationData().removeObservers(viewLifecycleOwner)
    }
}
