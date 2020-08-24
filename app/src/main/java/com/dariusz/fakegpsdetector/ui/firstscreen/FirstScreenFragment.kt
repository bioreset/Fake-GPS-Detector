package com.dariusz.fakegpsdetector.ui.firstscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.databinding.HomescreenBinding
import com.dariusz.fakegpsdetector.model.CellTowerModel
import com.dariusz.fakegpsdetector.model.LocationModel
import com.dariusz.fakegpsdetector.model.RoutersListModel
import com.dariusz.fakegpsdetector.ui.secondscreen.SecondScreenViewModel
import com.dariusz.fakegpsdetector.ui.thirdscreen.ThirdScreenViewModel
import com.dariusz.fakegpsdetector.utils.DistanceCalculator.calculateDistance
import com.dariusz.fakegpsdetector.utils.DistanceCalculator.isRealLocation
import com.dariusz.fakegpsdetector.utils.Injectors.provideFirstScreenViewModelFactory
import com.dariusz.fakegpsdetector.utils.Injectors.provideSecondScreenViewModelFactory
import com.dariusz.fakegpsdetector.utils.Injectors.provideThirdScreenViewModelFactory
import com.dariusz.fakegpsdetector.utils.ViewUtils.performActionInsideCoroutine
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class FirstScreenFragment : Fragment() {

    private val firstScreenViewModel: FirstScreenViewModel by viewModels {
        provideFirstScreenViewModelFactory(requireContext())
    }

    private val secondScreenViewModel: SecondScreenViewModel by viewModels {
        provideSecondScreenViewModelFactory(requireContext())
    }

    private val thirdScreenViewModel: ThirdScreenViewModel by viewModels {
        provideThirdScreenViewModelFactory(requireContext())
    }

    private var googleMapObject: GoogleMap? = null

    private var routersList: List<RoutersListModel> = ArrayList()

    private var cellTowersList: List<CellTowerModel> = ArrayList()

    private var homeScreenBindingImpl: HomescreenBinding? = null

    private val homeScreenBinding
        get() = homeScreenBindingImpl!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeScreenBindingImpl = HomescreenBinding.inflate(inflater, container, false)
        return homeScreenBinding.root
    }

    @InternalCoroutinesApi
    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        performActionInsideCoroutine(viewLifecycleOwner) {
            googleMapObject = mapFragment.awaitMap()
        }
        performMain()
        homeScreenBinding.refreshData.setOnClickListener {
            performActionInsideCoroutine(viewLifecycleOwner) {
                performCheck()
                getStatus(checkStatusAfterAction())
            }
        }
    }

    private fun performMain() =
        fetchLocationData().observe(
            viewLifecycleOwner,
            {
                googleMapObject!!.addMarker(
                    MarkerOptions().position(LatLng(it.latitude, it.longitude))
                        .title("Your current location: ${it.latitude}, ${it.longitude} ")
                ).remove()
                googleMapObject!!.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(it.latitude, it.longitude),
                        18F
                    )
                )
                startLocationUpdate(it)
                performActionInsideCoroutine(viewLifecycleOwner) {
                    addToDb(it)
                }
            }
        )

    private fun fetchLocationData() = firstScreenViewModel.locationData(requireContext())

    private fun repoLocationConnection() = firstScreenViewModel.repoLocation

    private fun repoResultConnection() = firstScreenViewModel.repoResult

    private fun repoRoutersListConnection() = secondScreenViewModel.repo

    private fun repoCellTowersConnection() = thirdScreenViewModel.repo

    private suspend fun selectAllLocationData() =
        repoLocationConnection().selectAll()

    private suspend fun selectAllResultData() =
        repoResultConnection().selectAll()

    private suspend fun checkLocationStatus() =
        repoResultConnection().checkLocationStatus()

    private suspend fun insertData(location: LocationModel) =
        repoLocationConnection().insertAsFresh(location)

    private suspend fun getRoutersListMap() =
        repoRoutersListConnection().selectAll().let {
            routersList = it!!
        }

    private suspend fun getCellTowersListMap() =
        repoCellTowersConnection().selectAll().let {
            cellTowersList = it!!
        }

    private suspend fun performCheck(): Unit? {
        getRoutersListMap()
        getCellTowersListMap()
        return repoResultConnection().manageResponseAfterAction(
            cellTowersList,
            routersList
        )
    }

    private suspend fun getStatus(result: String?) {
        if (result != null) {
            when (result) {
                "location" -> {
                    homeScreenBinding.resultTitle.text = isTrueLocationString()
                    homeScreenBinding.resultTitle.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.common_google_signin_btn_text_light
                        )
                    )
                }
                else -> {
                    homeScreenBinding.resultTitle.text =
                        getString(R.string.error_checking_location_text)
                    homeScreenBinding.resultTitle.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.design_default_color_error
                        )
                    )
                }
            }
        } else {
            homeScreenBinding.resultTitle.text = getString(R.string.empty_db_text)
            homeScreenBinding.resultTitle.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.design_default_color_error
                )
            )
        }
    }

    private fun startLocationUpdate(location: LocationModel) {
        homeScreenBinding.longitudeValue.text = location.longitude.toString()
        homeScreenBinding.latitudeValue.text = location.latitude.toString()
    }

    private suspend fun addToDb(location: LocationModel): Unit? {
        return insertData(location)
    }

    private suspend fun isTrueLocation(): Boolean {
        val calculator = calculateDistance(
            selectAllLocationData()?.latitude ?: 0.0,
            selectAllLocationData()?.longitude ?: 0.0,
            selectAllResultData()?.lat ?: 0.0,
            selectAllResultData()?.lng ?: 0.0
        )
        return isRealLocation(
            calculator,
            selectAllResultData()?.accuracy ?: 0
        )
    }

    private suspend fun isTrueLocationString(): String {
        return if (isTrueLocation()) {
            getString(R.string.true_location_text)
        } else {
            getString(R.string.spoofed_location_text) +
                " . The distance to real location is more than " + checkLocationStatus()?.accuracy + " meters."
        }
    }

    private suspend fun checkStatusAfterAction(): String {
        return if (checkLocationStatus()?.lat != null) {
            "location"
        } else {
            "error"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fetchLocationData().removeObservers(viewLifecycleOwner)
        homeScreenBindingImpl = null
    }
}
