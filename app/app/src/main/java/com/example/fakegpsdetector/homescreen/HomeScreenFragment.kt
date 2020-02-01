package com.example.fakegpsdetector.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fakegpsdetector.R
import kotlinx.android.synthetic.main.homescreen.*

class HomeScreenFragment : Fragment() {

    private lateinit var homeScreenViewModel: HomeScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.homescreen, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        homeScreenViewModel = ViewModelProvider(this).get(HomeScreenViewModel::class.java)
        homeScreenViewModel.text.observe(this, Observer {
            text_homescreen2.text = it
        })

    }
}