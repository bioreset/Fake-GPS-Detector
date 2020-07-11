package com.dariusz.fakegpsdetector.ui.thirdscreen

import android.os.Bundle
import android.telephony.CellInfoLte
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.model.CellTowerModel.Companion.newCellTowersList
import com.dariusz.fakegpsdetector.ui.adapters.CellTowersListAdapter
import com.dariusz.fakegpsdetector.utils.Injectors
import com.dariusz.fakegpsdetector.utils.ViewUtils.showOnFragment
import kotlinx.android.synthetic.main.celltower_list.*

class ThirdScreenFragment : Fragment(R.layout.celltower_list) {

    private var listAdapterCell: CellTowersListAdapter? = null

    private val thirdScreenViewModel: ThirdScreenViewModel by viewModels {
        Injectors.provideThirdScreenViewModelFactory(requireContext())
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

    private fun updateItems(cellTowersList: List<CellInfoLte>? = null) {
        listAdapterCell?.clear()
        if (cellTowersList != null) {
            listAdapterCell?.addAll(newCellTowersList(cellTowersList)!!)
        }
        listAdapterCell?.notifyDataSetChanged()

    }

    private fun fetchNewCellTowerData() = thirdScreenViewModel.cellTowersData

    private fun repoConnection() = thirdScreenViewModel.repo

    private suspend fun addToDb(cellTowersList: List<CellInfoLte>?) {
        if (cellTowersList != null) {
            return repoConnection().insertAsFresh(newCellTowersList(cellTowersList)!!)
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
