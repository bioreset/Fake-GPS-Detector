package com.dariusz.fakegpsdetector.ui.thirdscreen

import android.os.Bundle
import android.telephony.CellInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.databinding.CelltowerListBinding
import com.dariusz.fakegpsdetector.ui.adapters.CellTowersListAdapter
import com.dariusz.fakegpsdetector.utils.CellTowersUtils.mapCellTowers
import com.dariusz.fakegpsdetector.utils.Injectors.provideThirdScreenViewModelFactory
import com.dariusz.fakegpsdetector.utils.ViewUtils.observeOnce
import com.dariusz.fakegpsdetector.utils.ViewUtils.performActionInsideCoroutineWithLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class ThirdScreenFragment :
    Fragment(R.layout.celltower_list),
    SwipeRefreshLayout.OnRefreshListener {

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

    override fun onRefresh() {
        cellTowerListBinding.swipeCelltowers.apply {
            fetchNewCellTowerData().observeOnce(
                viewLifecycleOwner,
                Observer {
                    updateItems(it)
                }
            )
            isRefreshing = false
        }
    }
}
