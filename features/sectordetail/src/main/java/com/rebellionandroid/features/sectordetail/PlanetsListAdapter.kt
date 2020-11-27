package com.rebellionandroid.features.sectordetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.features.sectorsdetail.R


class PlanetsListAdapter(
    private val planetsWithUnits: List<PlanetWithUnits>
) : RecyclerView.Adapter<PlanetsListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val planetsWithUnits: List<PlanetWithUnits>) : RecyclerView.ViewHolder(
        view
    ) {
        val planetName: TextView = view.findViewById(R.id.planet_name)

        init {
//            view.setOnClickListener {
//                val sectorWithPlanets = sectorsWithPlanets[adapterPosition]
//                println("click sector ${sectorWithPlanets.sector.name}")
//                val intent = Intent(it.context, SectorDetailActivity::class.java)
//                intent.putExtra("SELECTED_SECTOR_ID", sectorWithPlanets.sector.id);
//                it.context.startActivity(intent)
//            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_planet, viewGroup, false)
        return ViewHolder(view, planetsWithUnits)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val planet = planetsWithUnits[position].planet
        viewHolder.planetName.text = planet.name
    }

    override fun getItemCount() =  planetsWithUnits.size

}