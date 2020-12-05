package com.rebellionandroid.features.sectorslist

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.rebellionandroid.features.sectordetail.SectorDetailActivity
import com.rebellionandroid.features.sectordetail.SectorDetailFragment
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.SectorWithPlanets
import com.rebllelionandroid.features.sectorsList.R

class SectorListAdapter(
    private val sectors: List<SectorWithPlanets>
) : RecyclerView.Adapter<SectorListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val sectorsWithPlanets: List<SectorWithPlanets>) : RecyclerView.ViewHolder(
        view
    ) {
        val sectorName: TextView = view.findViewById(R.id.sector_name)
        val planetsList: RecyclerView  = view.findViewById(R.id.planets_list)

        init {
            view.setOnClickListener {
                val sectorWithPlanets = sectorsWithPlanets[adapterPosition]
                val sectorId = sectorWithPlanets.sector.id
                println("Sending sectorId:${sectorId}")
                val bundle = bundleOf("sectorId" to sectorId)
                it.findNavController().navigate(R.id.sector_detail_graph, bundle)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_sector, viewGroup, false)
        return ViewHolder(view, sectors)
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