package com.dariusz.fakegpsdetector.ui.thirdscreen

import android.os.Bundle
import android.telephony.CellInfo
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.ui.adapters.CellTowersListAdapter
import com.dariusz.fakegpsdetector.utils.CellTowersUtils.mapCellTowers
import com.dariusz.fakegpsdetector.utils.Injectors.provideThirdScreenViewModelFactory
import com.dariusz.fakegpsdetector.utils.ViewUtils.performActionInsideCoroutineWithLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.celltower_list.*
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class ThirdScreenFragment : Fragment(R.layout.celltower_list) {

    private var listAdapterCell: CellTowersListAdapter? = null

    private val thirdScreenViewModel: ThirdScreenViewModel by viewModels {
        provideThirdScreenViewModelFactory(requireContext())
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapterCell = CellTowersListAdapter(requireContext())

        celltowerlist.adapter = listAdapterCell

        performActionInsideCoroutineWithLiveData(
            fetchNewCellTowerData().distinctUntilChanged(),
            viewLifecycleOwner,
            actionInCoroutine = {
                addToDb(it)
            },
            actionOnMain = {
                updateItems(it)
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
        listAdapterCell?.clear()
        if (cellTowersList != null) {
            listAdapterCell?.addAll(mapCellTowers(cellTowersList))
        }
        listAdapterCell?.notifyDataSetChanged()
    }

    private fun restoreList() {
        fetchNewCellTowerData().value?.let {
            updateItems(it)
        }
    }

    override fun onResume() {
        super.onResume()
        restoreList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fetchNewCellTowerData().removeObservers(viewLifecycleOwner)
        celltowerlist.adapter = null
    }
}
