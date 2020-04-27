package com.dariusz.fakegpsdetector.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.model.CellTowerModel
import kotlinx.android.synthetic.main.celltower_item.view.*

class CellTowersListAdapter(context: Context) : ArrayAdapter<CellTowerModel>(context, 0) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    @Override
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val view = convertView ?: inflater.inflate(R.layout.celltower_item, parent, false)

        view.txt_cellid.text = item?.cellId.toString()
        view.txt_locationareacode.text = item?.locationAreaCode.toString()
        view.txt_mobilecountrycode.text = item?.mobileCountryCode
        view.mobilenetworkcode.text = item?.mobileNetworkCode
        view.signalstrength.text =
            context.getString(R.string.station_level, item?.signalStrength.toString())
        return view
    }

}