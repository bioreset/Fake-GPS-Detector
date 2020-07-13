package com.dariusz.fakegpsdetector.ui.thirdscreen

import android.os.Bundle
import android.telephony.CellInfo
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.ui.adapters.CellTowersListAdapter
import com.dariusz.fakegpsdetector.utils.CellTowersUtils.detectCellTowerType
import com.dariusz.fakegpsdetector.utils.CellTowersUtils.newCellTowersList
import com.dariusz.fakegpsdetector.utils.Injectors.provideThirdScreenViewModelFactory
import com.dariusz.fakegpsdetector.utils.ViewUtils.showOnFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.celltower_list.*

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

        showOnFragment(
            fetchNewCellTowerData(),
            viewLifecycleOwner,
            actionInCoroutine = {
                addToDb(it)
            },
            actionOnMain = {
                updateItems(it)
            }
        )
    }

    private fun updateItems(cellTowersList: List<CellInfo>? = null) {
        listAdapterCell?.clear()
        if (cellTowersList != null) {
                newCellTowersList(
                    detectCellTowerType(fetchCellTowersTypeData()),
                    cellTowersList
                )?.let {
                    listAdapterCell?.addAll(it)
                }
        }
        listAdapterCell?.notifyDataSetChanged()

    }

    private fun fetchNewCellTowerData() = thirdScreenViewModel.cellTowersData(requireContext())

    private fun fetchCellTowersTypeData() = thirdScreenViewModel.cellTowersType(requireContext()).value

    private fun repoConnection() = thirdScreenViewModel.repo

    private suspend fun addToDb(cellTowersList: List<CellInfo>?): Unit? {
        return if (cellTowersList != null) {
             newCellTowersList(
                detectCellTowerType(
                    fetchCellTowersTypeData()
                ), cellTowersList
            )?.let {
                repoConnection().insertAsFresh(
                    it
                )
            }
        } else {
            null
        }
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
        celltowerlist.adapter = null
    }


}
