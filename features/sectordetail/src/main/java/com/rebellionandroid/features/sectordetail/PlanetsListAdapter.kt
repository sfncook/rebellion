package com.rebellionandroid.features.sectordetail

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.rebellionandroid.features.planetdetail.PlanetDetailActivity
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.features.sectorsdetail.R


class PlanetsListAdapter(
    private val planetsWithUnits: List<PlanetWithUnits>
) : RecyclerView.Adapter<PlanetsListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val planetsWithUnits: List<PlanetWithUnits>) : RecyclerView.ViewHolder(
        view
    ) {
        val planetName: TextView = view.findViewById(R.id.planet_name)
        val planetLoyaltyImg: ImageView = view.findViewById(R.id.planet_loyalty)
        val planetHasShipImg: ImageView = view.findViewById(R.id.planet_has_ship)
        val planetHasUnitsImg: ImageView = view.findViewById(R.id.planet_has_units)
        val planetHasDefenseImg: ImageView = view.findViewById(R.id.planet_has_defense)
        val planetHasFactoriesImg: ImageView = view.findViewById(R.id.planet_has_factories)

        init {
            view.setOnClickListener {
                val planetWithUnits = planetsWithUnits[adapterPosition]
                val planetId = planetWithUnits.planet.id
                val bundle = bundleOf("planetId" to planetId)
                it.findNavController().navigate(R.id.planet_detail_graph, bundle)
//                val planetWithUnits = planetsWithUnits[adapterPosition]
//                println("click planet ${planetWithUnits.planet.name}")
//                val intent = Intent(it.context, PlanetDetailActivity::class.java)
//                intent.putExtra("SELECTED_PLANET_ID", planetWithUnits.planet.id);
//                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_planet, viewGroup, false)
        return ViewHolder(view, planetsWithUnits)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val planetWithUnit = planetsWithUnits[position]
        val planet = planetWithUnit.planet
        viewHolder.planetName.text = planet.name

        viewHolder.planetHasShipImg.visibility = if(planetWithUnit.ships.isEmpty()) GONE else VISIBLE
        viewHolder.planetHasUnitsImg.visibility = if(planetWithUnit.units.isEmpty()) GONE else VISIBLE
        viewHolder.planetHasDefenseImg.visibility = if(planetWithUnit.defenseStructures.isEmpty()) GONE else VISIBLE
        viewHolder.planetHasFactoriesImg.visibility = if(planetWithUnit.factories.isEmpty()) GONE else VISIBLE

        val (imgId, colorId) = Utilities.getLoyaltyIconForPlanet(planet)
        viewHolder.planetLoyaltyImg.setImageResource(imgId)
        viewHolder.planetLoyaltyImg.setColorFilter(
                ContextCompat.getColor(
                        viewHolder.itemView.context,
                        colorId
                ), android.graphics.PorterDuff.Mode.MULTIPLY
        )
    }

    override fun getItemCount() =  planetsWithUnits.size

}
