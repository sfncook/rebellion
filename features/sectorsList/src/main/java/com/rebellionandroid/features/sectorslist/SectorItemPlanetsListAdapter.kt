package com.rebellionandroid.features.sectorslist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.gamestate.SectorWithPlanets
import com.rebllelionandroid.features.sectorsList.R

class SectorItemPlanetsListAdapter(
        private val planets: List<PlanetWithUnits>
) : RecyclerView.Adapter<SectorItemPlanetsListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val planetName: TextView = view.findViewById(R.id.planet_name)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.list_item_sector_item_planet, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.planetName.text = planets[position].planet.name
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() =  planets.size

}
