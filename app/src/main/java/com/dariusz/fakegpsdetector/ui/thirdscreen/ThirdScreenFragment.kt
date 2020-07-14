package com.dariusz.fakegpsdetector.ui.thirdscreen

import android.os.Bundle
import android.telephony.CellInfo
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.model.CellTowerType
import com.dariusz.fakegpsdetector.ui.adapters.CellTowersListAdapter
import com.dariusz.fakegpsdetector.utils.CellTowersUtils.detectCellTowerType
import com.dariusz.fakegpsdetector.utils.CellTowersUtils.newCellTowersList
import com.dariusz.fakegpsdetector.utils.Injectors.provideThirdScreenViewModelFactory
import com.dariusz.fakegpsdetector.utils.ViewUtils.performActionInsideCoroutineWithLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.celltower_list.*

@AndroidEntryPoint
class ThirdScreenFragment : Fragment(R.layout.celltower_list) {

    private var listAdapterCell: CellTowersListAdapter? = null

    private var networkType: CellTowerType? = null

    private val thirdScreenViewModel: ThirdScreenViewModel by viewModels {
        provideThirdScreenViewModelFactory(requireContext())
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapterCell = CellTowersListAdapter(requireContext())

        celltowerlist.adapter = listAdapterCell

        performActionInsideCoroutineWithLiveData(
            fetchNewCellTowerData(),
            viewLifecycleOwner,
            actionInCoroutine = {
                addToDb(it)
            },
            actionOnMain = {
                updateItems(it)
            }
        )

        fetchCellTowersTypeData().observe(
            viewLifecycleOwner,
            Observer {
                networkType = detectCellTowerType(it)
            }
        )
    }

    private fun fetchNewCellTowerData() = thirdScreenViewModel.cellTowersData(requireContext())

    private fun fetchCellTowersTypeData() = thirdScreenViewModel.cellTowersType(requireContext())

    private fun repoConnection() = thirdScreenViewModel.repo

    private suspend fun addToDb(cellTowersList: List<CellInfo>?): Unit? {
        return if (cellTowersList != null) {
            newCellTowersList(
                networkType,
                cellTowersList
            )?.let {
                repoConnection().insertAsFresh(
                    it
                )
            }
        } else {
            null
        }
    }

    private fun updateItems(cellTowersList: List<CellInfo>? = null) {
        listAdapterCell?.clear()
        if (cellTowersList != null) {
            newCellTowersList(
                networkType,
                cellTowersList
            )?.let {
                listAdapterCell?.addAll(it)
            }
        }
        listAdapterCell?.notifyDataSetChanged()
    }

    private fun restoreList() {
        fetchNewCellTowerData().value?.let {
            updateItems(it)
        }
    }

    override fun onResume() {
        restoreList()
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fetchNewCellTowerData().removeObservers(viewLifecycleOwner)
        fetchCellTowersTypeData().removeObservers(viewLifecycleOwner)
        celltowerlist.adapter = null
    }
}
