package com.example.fakegpsdetector.celltowers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fakegpsdetector.R
import kotlinx.android.synthetic.main.celltowers.*

class CellTowersFragment : Fragment() {

    private lateinit var cellTowersViewModel: CellTowersViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.celltowers, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cellTowersViewModel = ViewModelProvider(this).get(CellTowersViewModel::class.java)
        cellTowersViewModel.text.observe(this, Observer {
            text_celltowers.text = it
        })

    }
}