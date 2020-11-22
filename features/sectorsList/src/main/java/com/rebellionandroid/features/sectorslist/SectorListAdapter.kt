package com.rebellionandroid.features.sectorslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.database.gamestate.Sector
import com.rebllelionandroid.features.sectorsList.R

class SectorListAdapter(private val sectors: List<Sector>) :
    RecyclerView.Adapter<SectorListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sectorName: TextView = view.findViewById(R.id.sector_name)
        val manyPlanets: TextView = view.findViewById(R.id.many_planets)

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
        viewHolder.sectorName.text = sectors[position].name
//        viewHolder.manyPlanets.text = sectors[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() =  sectors.size

}