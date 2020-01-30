package com.example.fakegpsdetector.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fakegpsdetector.R

class HomeScreenFragment : Fragment() {

    private lateinit var homeScreenViewModel: HomeScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeScreenViewModel =
            ViewModelProvider(this).get(HomeScreenViewModel::class.java)
        val root = inflater.inflate(R.layout.homescreen, container, false)
        val textView: TextView = root.findViewById(R.id.text_homescreen)
        homeScreenViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}