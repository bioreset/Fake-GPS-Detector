package com.example.fakegpsdetector.celltowers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fakegpsdetector.R

class CellTowersFragment : Fragment() {

    private lateinit var cellTowersViewModel: CellTowersViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cellTowersViewModel =
            ViewModelProvider(this).get(CellTowersViewModel::class.java)
        val root = inflater.inflate(R.layout.routers, container, false)
        val textView: TextView = root.findViewById(R.id.text_celltowers)
        cellTowersViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}