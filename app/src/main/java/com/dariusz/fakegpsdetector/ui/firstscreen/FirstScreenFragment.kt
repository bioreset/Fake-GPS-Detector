package com.dariusz.fakegpsdetector.ui.firstscreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.model.LocationModel
import com.dariusz.fakegpsdetector.utils.DistanceCalculator.calculateDistance
import com.dariusz.fakegpsdetector.utils.DistanceCalculator.isRealLocation
import com.dariusz.fakegpsdetector.utils.FlowUtils.collectTheFlow
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

@InternalCoroutinesApi
@AndroidEntryPoint
class FirstScreenFragment : Fragment(R.layout.homescreen), OnMapReadyCallback {

    private val firstScreenViewModel: FirstScreenViewModel by viewModels {
        provideFirstScreenViewModelFactory(requireContext())
    }

    private var googleMapObject: GoogleMap? = null

    @InternalCoroutinesApi
    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                startLocationUpdate(it)
            },
            actionOnMain = {
                googleMapObject!!.addMarker(
                    MarkerOptions().position(LatLng(it!!.latitude, it.longitude))
                        .title("Your current location: ${it.latitude}, ${it.longitude} ")
                ).remove()
                googleMapObject!!.moveCamera(
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

    private suspend fun performCheck() =
        collectTheFlow(repoResultConnection().performAction(requireContext()))

    private suspend fun selectAllLocationData() =
        collectTheFlow(repoLocationConnection().selectAll())

    private suspend fun selectAllResultData() = collectTheFlow(repoResultConnection().selectAll())

    private suspend fun checkLocationStatus() =
        collectTheFlow(repoResultConnection().checkLocationStatus())

    private suspend fun insertData(location: LocationModel) =
        collectTheFlow(repoLocationConnection().insertAsFresh(location))

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

    private fun startLocationUpdate(location: LocationModel) {
        longitude_value.text = location.longitude.toString()
        latitude_value.text = location.latitude.toString()
    }

    private suspend fun addToDb(location: LocationModel) {
        return insertData(location)
    }

    private suspend fun isTrueLocation(): Boolean {
        val calculator = calculateDistance(
            selectAllLocationData().latitude,
            selectAllLocationData().longitude,
            selectAllResultData().lat ?: 0.0,
            selectAllResultData().lng ?: 0.0
        )
        return isRealLocation(
            calculator,
            selectAllResultData().accuracy ?: 0
        )
    }

    private suspend fun isTrueLocationString(): String {
        return if (isTrueLocation()) {
            getString(R.string.true_location_text)
        } else {
            getString(R.string.spoofed_location_text)
        }
    }

    private suspend fun checkStatusAfterAction() =
        checkLocationStatus().status

    override fun onDestroyView() {
        super.onDestroyView()
        fetchLocationData().removeObservers(viewLifecycleOwner)
    }
}
