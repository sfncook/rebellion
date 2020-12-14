package com.rebellionandroid.components.sectorWithPlanetsFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.GameState
import com.rebllelionandroid.core.database.gamestate.SectorWithPlanets

class SectorListAdapter(
    private val sectors: List<SectorWithPlanets>,
    private val onSelectCallback: (sectorId: Long) -> Unit
) : RecyclerView.Adapter<SectorListAdapter.ViewHolder>() {

    class ViewHolder(
        view: View,
        private val sectorsWithPlanets: List<SectorWithPlanets>,
        private val onSelectCallback: (sectorId: Long) -> Unit
        ) : RecyclerView.ViewHolder(
        view
    ) {
        val sectorName: TextView = view.findViewById(R.id.sector_name)
        val planetsList: RecyclerView  = view.findViewById(R.id.planets_list)
        init {
            view.setOnClickListener {
                val sectorWithPlanets = sectorsWithPlanets[adapterPosition]
                val sectorId = sectorWithPlanets.sector.id
                onSelectCallback(sectorId)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_sector, viewGroup, false)
        return ViewHolder(view, sectors, onSelectCallback)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val sector = sectors[position].sector
        viewHolder.sectorName.text = sector.name

        val planets = sectors[position].planets
        val viewAdapter = SectorItemPlanetsListAdapter(Utilities.sortPlanets(planets))
        viewHolder.planetsList.adapter = viewAdapter
        viewHolder.planetsList.apply {
            adapter = viewAdapter
        }
    }

    override fun getItemCount() =  sectors.size

}