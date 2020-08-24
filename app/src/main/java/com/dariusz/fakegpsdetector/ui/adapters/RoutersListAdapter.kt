package com.dariusz.fakegpsdetector.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.databinding.RoutersItemBinding
import com.dariusz.fakegpsdetector.model.RoutersListModel

class RoutersListAdapter :
    RecyclerView.Adapter<RoutersListAdapter.RoutersViewHolder>() {

    private var routersList: List<RoutersListModel> = ArrayList()

    private var currentContext: Context? = null

    private lateinit var routersListBinding: RoutersItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutersViewHolder {
        currentContext = parent.context
        routersListBinding =
            RoutersItemBinding.inflate(LayoutInflater.from(currentContext), parent, false)
        return RoutersViewHolder(routersListBinding)
    }

    override fun onBindViewHolder(holder: RoutersViewHolder, position: Int) {
        val currentItem = routersList[position]
        holder.txtSSID.text = currentContext!!.getString(R.string.ssid_text, currentItem.ssid)
        holder.txtBSSID.text = currentContext!!.getString(R.string.mac_text, currentItem.macAddress)
        holder.txtFrequency.text =
            currentContext!!.getString(R.string.station_frequency, currentItem.frequency.toString())
        holder.txtSignalStrength.text =
            currentContext!!.getString(R.string.station_level, currentItem.level.toString())
    }

    override fun getItemCount() = routersList.size

    fun submitList(routerList: List<RoutersListModel>) {
        val oldList = routersList
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(BlogItemDiffCallback(oldList, routerList))
        routersList = routerList
        diffResult.dispatchUpdatesTo(this)
    }

    fun clearList() {
        routersList = emptyList()
    }

    inner class RoutersViewHolder(itemView: RoutersItemBinding) :
        RecyclerView.ViewHolder(routersListBinding.root) {
        val txtSSID: TextView = itemView.txtSsid
        val txtBSSID: TextView = itemView.txtBssid
        val txtFrequency: TextView = itemView.txtFrequency
        val txtSignalStrength: TextView = itemView.txtLevel
    }

    class BlogItemDiffCallback(
        private var oldBlogList: List<RoutersListModel>,
        private var newBlogList: List<RoutersListModel>

    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return (
                oldBlogList[oldItemPosition].id
                    == newBlogList[newItemPosition].id
                )
        }

        override fun getOldListSize(): Int {
            return oldBlogList.size
        }

        override fun getNewListSize(): Int {
            return newBlogList.size
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return (
                oldBlogList[oldItemPosition]
                    == newBlogList[newItemPosition]
                )
        }
    }
}
