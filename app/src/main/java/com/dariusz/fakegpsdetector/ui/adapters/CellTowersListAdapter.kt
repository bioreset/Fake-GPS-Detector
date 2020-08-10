package com.dariusz.fakegpsdetector.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.databinding.CelltowerItemBinding
import com.dariusz.fakegpsdetector.model.CellTowerModel

class CellTowersListAdapter :
    RecyclerView.Adapter<CellTowersListAdapter.CellTowersViewHolder>() {

    private var cellTowersList: List<CellTowerModel> = ArrayList()

    private var currentContext: Context? = null

    private lateinit var celltowerItemBinding: CelltowerItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellTowersViewHolder {
        currentContext = parent.context
        celltowerItemBinding =
            CelltowerItemBinding.inflate(LayoutInflater.from(currentContext), parent, false)
        return CellTowersViewHolder(celltowerItemBinding)
    }

    override fun onBindViewHolder(holder: CellTowersViewHolder, position: Int) {
        val currentItem = cellTowersList[position]
        holder.txtCellId.text = currentContext!!.getString(R.string.cid_text, currentItem.cellId)
        holder.txtLAC.text =
            currentContext!!.getString(R.string.lac_text, currentItem.locationAreaCode.toString())
        holder.txtMCC.text =
            currentContext!!.getString(R.string.mcc_text, currentItem.mobileCountryCode)
        holder.txtMNC.text =
            currentContext!!.getString(R.string.mnc_text, currentItem.mobileNetworkCode)
        holder.txtSignalStrength.text = currentContext!!.getString(
            R.string.station_level,
            currentItem.signalStrength.toString()
        )
    }

    override fun getItemCount() = cellTowersList.size

    fun submitList(cellTowerList: List<CellTowerModel>) {
        val oldList = cellTowersList
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(BlogItemDiffCallback(oldList, cellTowerList))
        cellTowersList = cellTowerList
        diffResult.dispatchUpdatesTo(this)
    }

    fun clearList() {
        cellTowersList = emptyList()
    }

    inner class CellTowersViewHolder(itemView: CelltowerItemBinding) :
        RecyclerView.ViewHolder(celltowerItemBinding.root) {
        val txtCellId: TextView = itemView.txtCellid
        val txtLAC: TextView = itemView.txtLocationareacode
        val txtMCC: TextView = itemView.txtMobilecountrycode
        val txtMNC: TextView = itemView.mobilenetworkcode
        val txtSignalStrength: TextView = itemView.signalstrength
    }

    class BlogItemDiffCallback(
        private var oldBlogList: List<CellTowerModel>,
        private var newBlogList: List<CellTowerModel>

    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return (
                    oldBlogList[oldItemPosition].cellId
                            == newBlogList[newItemPosition].cellId
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
