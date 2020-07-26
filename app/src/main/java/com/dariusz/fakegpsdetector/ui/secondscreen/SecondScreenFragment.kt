package com.dariusz.fakegpsdetector.ui.secondscreen

import android.net.wifi.ScanResult
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.databinding.RoutersListBinding
import com.dariusz.fakegpsdetector.model.RoutersListModel.Companion.newRoutersList
import com.dariusz.fakegpsdetector.ui.adapters.RoutersListAdapter
import com.dariusz.fakegpsdetector.utils.Injectors.provideSecondScreenViewModelFactory
import com.dariusz.fakegpsdetector.utils.ViewUtils.observeOnce
import com.dariusz.fakegpsdetector.utils.ViewUtils.performActionInsideCoroutineWithLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class SecondScreenFragment : Fragment(R.layout.routers_list), SwipeRefreshLayout.OnRefreshListener {

    private var listAdapterWifi: RoutersListAdapter? = null

    private val secondScreenViewModel: SecondScreenViewModel by viewModels {
        provideSecondScreenViewModelFactory(requireContext())
    }

    private var routersListBindingImpl: RoutersListBinding? = null

    private val routersListBinding
        get() = routersListBindingImpl!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        routersListBindingImpl = RoutersListBinding.inflate(inflater, container, false)
        return routersListBinding.root
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        routersListBinding.routerlist.apply {
            layoutManager = LinearLayoutManager(requireContext())
            listAdapterWifi = RoutersListAdapter()
            adapter = listAdapterWifi
        }

        performActionInsideCoroutineWithLiveData(
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
        if (routersList != null) {
            listAdapterWifi?.submitList(newRoutersList(routersList))
        }
        listAdapterWifi?.notifyDataSetChanged()
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

    override fun onRefresh() {
        routersListBinding.swipeRouters.apply {
            fetchNewRoutersData().observeOnce(
                viewLifecycleOwner,
                Observer {
                    updateItems(it)
                }
            )
            isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fetchNewRoutersData().removeObservers(viewLifecycleOwner)
        routersListBinding.routerlist.adapter = null
        routersListBindingImpl = null
    }
}
