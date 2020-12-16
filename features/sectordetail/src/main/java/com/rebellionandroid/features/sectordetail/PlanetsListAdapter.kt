package com.rebellionandroid.features.sectordetail

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
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import com.rebllelionandroid.features.sectorsdetail.R


class PlanetsListAdapter(
    private val planetsWithUnits: List<PlanetWithUnits>
) : RecyclerView.Adapter<PlanetsListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val planetsWithUnits: List<PlanetWithUnits>) : RecyclerView.ViewHolder(
        view
    ) {
        val planetName: TextView = view.findViewById(R.id.planet_name)
        val planetLoyaltyImg: ImageView = view.findViewById(R.id.planet_loyalty)

        val planetHasShipImgTeamA: ImageView = view.findViewById(R.id.planet_has_ship_team_a)
        val manyShipsTeamA: TextView = view.findViewById(R.id.many_ships_team_a)

        val planetHasUnitsImgTeamA: ImageView = view.findViewById(R.id.planet_has_units_team_a)
        val manyUnitsTeamA: TextView = view.findViewById(R.id.many_units_team_a)

        val planetHasDefenseImgTeamA: ImageView = view.findViewById(R.id.planet_has_defense_team_a)
        val manyDefenseTeamA: TextView = view.findViewById(R.id.many_defense_team_a)

        val planetHasFactoriesImgTeamA: ImageView = view.findViewById(R.id.planet_has_factories_team_a)
        val manyFactoriesTeamA: TextView = view.findViewById(R.id.many_factories_team_a)

        init {
            view.setOnClickListener {
                val planetWithUnits = planetsWithUnits[adapterPosition]
                val planetId = planetWithUnits.planet.id
                val bundle = bundleOf("planetId" to planetId)
                it.findNavController().navigate(R.id.planet_detail_graph, bundle)
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


        val manyShipsTeamA = 0
        val manyShipsTeamB = 0
        planetWithUnit.shipsWithUnits.forEach { shipWithUnits ->
            when(shipWithUnits.ship.team) {
                TeamLoyalty.TeamA -> manyShipsTeamA.inc()
                TeamLoyalty.TeamB -> manyShipsTeamB.inc()
                else -> {}
            }
        }
        if(manyShipsTeamA>0) {
            viewHolder.planetHasShipImgTeamA.visibility = VISIBLE
            viewHolder.manyShipsTeamA.visibility = VISIBLE
            viewHolder.manyShipsTeamA.text = manyShipsTeamA.toString()
        } else {
            viewHolder.planetHasShipImgTeamA.visibility = GONE
            viewHolder.manyShipsTeamA.visibility = GONE
        }
//        if(manyShipsTeamB>0) {
//            viewHolder.planetHasShipImgTeamB.visibility = VISIBLE
//            viewHolder.manyShipsTeamB.visibility = VISIBLE
//            viewHolder.manyShipsTeamB.text = manyShipsTeamB.toString()
//        } else {
//            viewHolder.planetHasShipImgTeamB.visibility = GONE
//            viewHolder.manyShipsTeamB.visibility = GONE
//        }

        viewHolder.planetHasUnitsImgTeamA.visibility = if(planetWithUnit.units.isEmpty()) GONE else VISIBLE
        viewHolder.planetHasDefenseImgTeamA.visibility = if(planetWithUnit.defenseStructures.isEmpty()) GONE else VISIBLE
        viewHolder.planetHasFactoriesImgTeamA.visibility = if(planetWithUnit.factories.isEmpty()) GONE else VISIBLE

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
