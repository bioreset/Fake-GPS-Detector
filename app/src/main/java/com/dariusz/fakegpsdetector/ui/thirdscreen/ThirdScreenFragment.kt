package com.dariusz.fakegpsdetector.ui.thirdscreen

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.adapters.CellTowersListAdapter
import com.dariusz.fakegpsdetector.model.CellTowerModel.Companion.newCellTowersList
import kotlinx.android.synthetic.main.celltower_list.*

class ThirdScreenFragment : Fragment() {

    private lateinit var listAdapterCell: CellTowersListAdapter

    private lateinit var cellTowersData: ThirdScreenViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listAdapterCell =
            CellTowersListAdapter(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.celltower_list, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cellTowersData = ViewModelProvider(this).get(ThirdScreenViewModel::class.java)

        celltowerlist.adapter = listAdapterCell

        @Suppress("UNCHECKED_CAST")
        cellTowersData.getCellTowersData().observe(viewLifecycleOwner, Observer {
            updateItems(it)
        })
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun updateItems(celltowers: List<CellInfoLte>? = null) {
        listAdapterCell.clear()
        if (celltowers != null) {
            listAdapterCell.addAll(newCellTowersList(celltowers))
        }
        listAdapterCell.notifyDataSetChanged()

    }

}
