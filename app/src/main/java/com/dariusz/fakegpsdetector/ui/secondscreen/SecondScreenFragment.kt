package com.dariusz.fakegpsdetector.ui.secondscreen

import android.net.wifi.ScanResult
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.model.RoutersListModel.Companion.newRoutersList
import com.dariusz.fakegpsdetector.ui.adapters.RoutersListAdapter
import com.dariusz.fakegpsdetector.utils.Injectors.provideSecondScreenViewModelFactory
import com.dariusz.fakegpsdetector.utils.ViewUtils.performActionInsideCoroutineWithLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.routers_list.*
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class SecondScreenFragment : Fragment(R.layout.routers_list), SwipeRefreshLayout.OnRefreshListener {

    private var listAdapterWifi: RoutersListAdapter? = null

    private val secondScreenViewModel: SecondScreenViewModel by viewModels {
        provideSecondScreenViewModelFactory(requireContext())
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapterWifi = RoutersListAdapter(requireContext())

        routerlist.adapter = listAdapterWifi

        performActionInsideCoroutineWithLiveData(
            fetchNewRoutersData().distinctUntilChanged(),
            viewLifecycleOwner,
            actionInCoroutine = {
                addToDb(it)
            },
            actionOnMain = {
                updateItems(it)
            }
        )
    }

    private fun updateItems(routersList: List<ScanResult>? = null) {
        listAdapterWifi?.notifyDataSetChanged()
        if (routersList != null) {
            listAdapterWifi?.addAll(newRoutersList(routersList))
        }
    }

    private suspend fun addToDb(routersList: List<ScanResult>?) {
        if (routersList != null) {
            insertData(routersList)
        }
    }

    private fun fetchNewRoutersData() = secondScreenViewModel.wifiData(requireContext())

    private fun repoConnection() = secondScreenViewModel.repo

    private suspend fun insertData(routersList: List<ScanResult>) =
        repoConnection().insertAsFresh(newRoutersList(routersList))

    private fun restoreList() =
        fetchNewRoutersData().value?.let {
            updateItems(it)
        }


    override fun onDestroyView() {
        super.onDestroyView()
        fetchNewRoutersData().removeObservers(viewLifecycleOwner)
        routerlist.adapter = null
    }

    override fun onRefresh() {
        restoreList()
    }
}
