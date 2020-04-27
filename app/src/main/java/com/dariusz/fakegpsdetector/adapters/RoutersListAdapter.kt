package com.dariusz.fakegpsdetector.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.model.RouterModel
import kotlinx.android.synthetic.main.routers_item.view.*

class RoutersListAdapter(context: Context) : ArrayAdapter<RouterModel>(context, 0) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

     @Override
     override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val view = convertView ?: inflater.inflate(R.layout.routers_item, parent, false)

        view.txt_ssid.text = item?.ssid
        view.txt_bssid.text = item?.bssid
        view.txt_frequency.text = context.getString(R.string.station_frequency, item?.frequency.toString())
        view.txt_level.text = context.getString(R.string.station_level, item?.level.toString())

        return view
    }

}