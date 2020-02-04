package com.example.fakegpsdetector.mapview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fakegpsdetector.R
import kotlinx.android.synthetic.main.mapview.*

class MapViewFragment : Fragment() {

    private lateinit var mapViewViewModel: MapViewViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.mapview, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mapViewViewModel = ViewModelProvider(this).get(MapViewViewModel::class.java)
        mapViewViewModel.text.observe(this, Observer {
            text_mapview.text = it
        })
    }
}