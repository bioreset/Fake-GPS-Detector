package com.example.fakegpsdetector.routers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fakegpsdetector.R

class RoutersFragment : Fragment() {

    private lateinit var routersViewModel: RoutersViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        routersViewModel =
            ViewModelProvider(this).get(routersViewModel::class.java)
        val root = inflater.inflate(R.layout.routers, container, false)
        val textView: TextView = root.findViewById(R.id.text_routers)
        routersViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}