package com.rebellionandroid.features.sectorslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.database.gamestate.SectorWithPlanets
import com.rebllelionandroid.features.sectorsList.R

class SectorListAdapter(
        private val sectors: List<SectorWithPlanets>
) : RecyclerView.Adapter<SectorListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sectorName: TextView = view.findViewById(R.id.sector_name)
        val planetsList: RecyclerView  = view.findViewById(R.id.planets_list)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_sector, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val sector = sectors[position].sector
        viewHolder.sectorName.text = sector.name

        val planets = sectors[position].planets
        val viewAdapter = SectorItemPlanetsListAdapter(planets)
        viewHolder.planetsList.adapter = viewAdapter
        viewHolder.planetsList.apply {
            adapter = viewAdapter
        }
    }

    override fun getItemCount() =  sectors.size

}