package com.dariusz.fakegpsdetector.ui.thirdscreen

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.CellInfoLte
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.model.CellTowerModel.Companion.newCellTowersList
import com.dariusz.fakegpsdetector.ui.adapters.CellTowersListAdapter
import com.dariusz.fakegpsdetector.utils.Injectors
import kotlinx.android.synthetic.main.celltower_list.*
import kotlinx.coroutines.launch

class ThirdScreenFragment : Fragment(R.layout.celltower_list) {

    private lateinit var listAdapterCell: CellTowersListAdapter

    private val thirdScreenViewModel: ThirdScreenViewModel by viewModels {
        Injectors.provideThirdScreenViewModelFactory(requireContext())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listAdapterCell =
            CellTowersListAdapter(context)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchNewCellTowerData().observe(viewLifecycleOwner, Observer {
            viewLifecycleOwner.lifecycle.coroutineScope.launch {
                addToDb(it)
            }
            updateItems(it)
        })

        celltowerlist.adapter = listAdapterCell

        @Suppress("UNCHECKED_CAST")
        thirdScreenViewModel.cellTowersData.observe(viewLifecycleOwner, Observer {
            updateItems(it)
        })
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun updateItems(celltowers: List<CellInfoLte>? = null) {
        listAdapterCell.clear()
        if (celltowers != null) {
            listAdapterCell.addAll(newCellTowersList(celltowers)!!)
        }
        listAdapterCell.notifyDataSetChanged()

    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun fetchNewCellTowerData() = thirdScreenViewModel.cellTowersData

    @RequiresApi(Build.VERSION_CODES.P)
    private suspend fun addToDb(celltowerslist: List<CellInfoLte>?) {
        if (celltowerslist != null) {
            return thirdScreenViewModel.repo.insertAsFresh(newCellTowersList(celltowerslist)!!)
        }
    }


}
