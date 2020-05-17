package com.dariusz.fakegpsdetector.ui.secondscreen

import android.content.Context
import android.net.wifi.ScanResult
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.adapters.RoutersListAdapter
import com.dariusz.fakegpsdetector.model.RoutersListModel.Companion.newRoutersList
import kotlinx.android.synthetic.main.routers_list.*

class SecondScreenFragment : Fragment() {

    private lateinit var listAdapterWifi: RoutersListAdapter

    private lateinit var secondScreenViewModel: SecondScreenViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listAdapterWifi =
                RoutersListAdapter(context)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.routers_list, container, false)
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        secondScreenViewModel = ViewModelProvider(this).get(SecondScreenViewModel::class.java)

        routerlist.adapter = listAdapterWifi

        secondScreenViewModel.getWifiData().observe(viewLifecycleOwner, Observer {
            addToDb(it)
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

    private fun addToDb(routerslist: List<ScanResult>) {
        return secondScreenViewModel.repo.insert(newRoutersList(routerslist))
    }

}


