package com.dariusz.fakegpsdetector.ui.secondscreen

import android.net.wifi.ScanResult
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.model.RoutersListModel.Companion.newRoutersList
import com.dariusz.fakegpsdetector.ui.adapters.RoutersListAdapter
import com.dariusz.fakegpsdetector.utils.Injectors.provideSecondScreenViewModelFactory
import com.dariusz.fakegpsdetector.utils.ViewUtils.showOnFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.routers_list.*

@AndroidEntryPoint
class SecondScreenFragment : Fragment(R.layout.routers_list) {

    private var listAdapterWifi: RoutersListAdapter? = null

    private val secondScreenViewModel: SecondScreenViewModel by viewModels {
        provideSecondScreenViewModelFactory(requireContext())
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapterWifi = RoutersListAdapter(requireContext())

        routerlist.adapter = listAdapterWifi

        showOnFragment(
            fetchNewRoutersData(),
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
        listAdapterWifi?.clear()
        if (routersList != null) {
            listAdapterWifi?.addAll(newRoutersList(routersList))
        }
        listAdapterWifi?.notifyDataSetChanged()
    }

    private suspend fun addToDb(routersList: List<ScanResult>?) {
        if (routersList != null) {
            repoConnection().insertAsFresh(newRoutersList(routersList))
        }
    }

    private fun fetchNewRoutersData() = secondScreenViewModel.wifiData(requireContext())

    private fun repoConnection() = secondScreenViewModel.repo

    private fun restoreList() {
        fetchNewRoutersData().value?.let {
            updateItems(it)
        }
    }

    override fun onResume() {
        restoreList()
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fetchNewRoutersData().removeObservers(viewLifecycleOwner)
        routerlist.adapter = null
    }
}
