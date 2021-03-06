package com.dariusz.fakegpsdetector.ui.thirdscreen

import android.os.Bundle
import android.telephony.CellInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dariusz.fakegpsdetector.databinding.CelltowerListBinding
import com.dariusz.fakegpsdetector.ui.adapters.CellTowersListAdapter
import com.dariusz.fakegpsdetector.utils.CellTowersUtils.mapCellTowers
import com.dariusz.fakegpsdetector.utils.Injectors.provideThirdScreenViewModelFactory
import com.dariusz.fakegpsdetector.utils.ViewUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class ThirdScreenFragment :
    Fragment() {

    private var listAdapterCell: CellTowersListAdapter? = null

    private val thirdScreenViewModel: ThirdScreenViewModel by viewModels {
        provideThirdScreenViewModelFactory(requireContext())
    }

    private var cellTowerListBindingImpl: CelltowerListBinding? = null

    private val cellTowerListBinding
        get() = cellTowerListBindingImpl!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cellTowerListBindingImpl = CelltowerListBinding.inflate(inflater, container, false)
        return cellTowerListBinding.root
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cellTowerListBinding.celltowerlist.apply {
            layoutManager = LinearLayoutManager(requireContext())
            listAdapterCell = CellTowersListAdapter()
            adapter = listAdapterCell
        }

        fetchNewCellTowerData().observe(
            viewLifecycleOwner,
            {
                updateItems(it)
                ViewUtils.performActionInsideCoroutine(viewLifecycleOwner) {
                    addToDb(it)
                }
            }
        )
    }

    private fun fetchNewCellTowerData() = thirdScreenViewModel.cellTowersData(requireContext())

    private fun repoConnection() = thirdScreenViewModel.repo

    private suspend fun insertData(cellTowersList: List<CellInfo>?) =
        repoConnection().insertAsFresh(mapCellTowers(cellTowersList))

    private suspend fun addToDb(cellTowersList: List<CellInfo>?) {
        if (cellTowersList != null) {
            insertData(cellTowersList)
        }
    }

    private fun updateItems(cellTowersList: List<CellInfo>? = null) {
        listAdapterCell?.clearList()
        if (cellTowersList != null) {
            listAdapterCell?.submitList(mapCellTowers(cellTowersList))
        }
        listAdapterCell?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fetchNewCellTowerData().removeObservers(viewLifecycleOwner)
        cellTowerListBinding.celltowerlist.adapter = null
        cellTowerListBindingImpl = null
    }
}
