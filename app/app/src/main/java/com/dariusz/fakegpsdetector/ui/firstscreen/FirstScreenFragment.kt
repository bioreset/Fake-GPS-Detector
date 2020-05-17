package com.dariusz.fakegpsdetector.ui.firstscreen

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.dariusz.fakegpsdetector.ui.MainInterface
import com.dariusz.fakegpsdetector.ui.SharedViewModel
import com.dariusz.fakegpsdetector.utils.Calculate
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

    private lateinit var mainInterface: MainInterface

    private lateinit var calculate: Calculate

    private var resultx : String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainInterface = context as MainInterface
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
        firstScreenViewModel.repo_result = context?.let { LocationFromApiResponseRepository.getInstance(it) }!!
        startLocationUpdate()
        (childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.getMapAsync(this)
        calculate = Calculate()
        refresh_data.setOnClickListener {
            mainInterface.doCheck()
            getStatus(mainInterface.returnStatus())
        }
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

    private fun getStatus(result: String?) {
        if (result != null) {
            when (result) {
                "location" -> {
                    result_title.text = isTrueLocationString()
                }
                else -> {
                    result_title.text = "Error: Something went wrong"
                }
            }
        }
        else {
            result_title.text = "Empty database"
        }
    }

    private fun startLocationUpdate() {
        firstScreenViewModel.getLocationData().observe(viewLifecycleOwner, Observer {
            longitude_value.text = it.longitude.toString()
            latitude_value.text = it.latitude.toString()
        })
    }

    private fun addToDb(location: LocationModel) {
        return firstScreenViewModel.repo_location.insert(location)
    }

    private fun showResult(): ApiResponseModel {
        return firstScreenViewModel.repo_result.selectAll()
    }

    private fun isTrueLocation(): Boolean {
        val calculator = calculate.calculateDistance(firstScreenViewModel.repo_location.selectAll().latitude,
                                    firstScreenViewModel.repo_location.selectAll().longitude,
                                    firstScreenViewModel.repo_result.selectAll().lat!!,
                                    firstScreenViewModel.repo_result.selectAll().lng!!)
        return calculate.isRealLocation(calculator, firstScreenViewModel.repo_result.selectAll().accuracy!!)
    }

    private fun isTrueLocationString(): String {
        resultx = if (isTrueLocation()){
            "True Location"
        }
        else {
            "Spoofed Location or too small sample size"
        }
        return resultx
    }

}
