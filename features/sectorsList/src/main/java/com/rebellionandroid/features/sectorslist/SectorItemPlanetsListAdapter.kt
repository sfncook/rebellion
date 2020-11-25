package com.rebellionandroid.features.sectorslist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.features.sectorsList.R

class SectorItemPlanetsListAdapter(context: Context, private var resource: Int, planets: List<PlanetWithUnits>) :
        ArrayAdapter<PlanetWithUnits>(context, resource, planets) {

    private var mInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: mInflater.inflate(resource, parent, false)

        val planetNameText = view.findViewById<TextView>(R.id.planet_name)
        val planetWithUnits = getItem(position)
        planetNameText.text = planetWithUnits?.planet?.name ?: "XXX"

        return view
    }
}
