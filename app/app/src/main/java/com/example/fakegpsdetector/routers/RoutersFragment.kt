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
import kotlinx.android.synthetic.main.routers.*

class RoutersFragment : Fragment() {

    private lateinit var routersViewModel: RoutersViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.routers, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        routersViewModel = ViewModelProvider(this).get(RoutersViewModel::class.java)
        routersViewModel.text.observe(this, Observer {
            text_routers.text = it
        })

    }
}