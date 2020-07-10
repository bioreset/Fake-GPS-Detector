package com.dariusz.fakegpsdetector.ui.secondscreen

import android.content.Context
import android.net.wifi.ScanResult
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.model.RoutersListModel.Companion.newRoutersList
import com.dariusz.fakegpsdetector.ui.adapters.RoutersListAdapter
import com.dariusz.fakegpsdetector.utils.Injectors
import kotlinx.android.synthetic.main.routers_list.*
import kotlinx.coroutines.launch

class SecondScreenFragment : Fragment(R.layout.routers_list) {

    private lateinit var listAdapterWifi: RoutersListAdapter

    private val secondScreenViewModel: SecondScreenViewModel by viewModels {
        Injectors.provideSecondScreenViewModelFactory(requireContext())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listAdapterWifi =
            RoutersListAdapter(context)
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        routerlist.adapter = listAdapterWifi

        secondScreenViewModel.wifiData.observe(viewLifecycleOwner, Observer {
            viewLifecycleOwner.lifecycle.coroutineScope.launch {
                addToDb(it)
            }
            updateItems(it)
        })

    }

    private fun updateItems(routers: List<ScanResult>? = null) {
        listAdapterWifi.clear()
        if (routers != null) {
            listAdapterWifi.addAll(newRoutersList(routers))
        }
        listAdapterWifi.notifyDataSetChanged()
    }

    private suspend fun addToDb(routerslist: List<ScanResult>) {
        return secondScreenViewModel.repo.insertAsFresh(newRoutersList(routerslist))
    }

}


