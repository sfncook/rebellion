package com.rebellionandroid.features.sectordetail

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
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

        val planetHasDefenseImg: ImageView = view.findViewById(R.id.planet_has_defense)
        val manyDefense: TextView = view.findViewById(R.id.many_defense)

        val planetHasFactoriesImg: ImageView = view.findViewById(R.id.planet_has_factories)
        val manyFactories: TextView = view.findViewById(R.id.many_factories)



        val planetHasShipImgTeamA: ImageView = view.findViewById(R.id.planet_has_ship_team_a)
        val manyShipsTeamA: TextView = view.findViewById(R.id.many_ships_team_a)

        val planetHasUnitsImgTeamA: ImageView = view.findViewById(R.id.planet_has_units_team_a)
        val manyUnitsTeamA: TextView = view.findViewById(R.id.many_units_team_a)



        val planetHasShipImgTeamB: ImageView = view.findViewById(R.id.planet_has_ship_team_b)
        val manyShipsTeamB: TextView = view.findViewById(R.id.many_ships_team_b)

        val planetHasUnitsImgTeamB: ImageView = view.findViewById(R.id.planet_has_units_team_b)
        val manyUnitsTeamB: TextView = view.findViewById(R.id.many_units_team_b)

        val energyList: LinearLayout = view.findViewById(R.id.planet_energy_imgs_list)

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


        var manyShipsTeamA = 0
        var manyShipsTeamB = 0
        planetWithUnit.shipsWithUnits.forEach { shipWithUnits ->
            when(shipWithUnits.ship.team) {
                TeamLoyalty.TeamA -> manyShipsTeamA = manyShipsTeamA.inc()
                TeamLoyalty.TeamB -> manyShipsTeamB = manyShipsTeamB.inc()
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

        if(manyShipsTeamB>0) {
            viewHolder.planetHasShipImgTeamB.visibility = VISIBLE
            viewHolder.manyShipsTeamB.visibility = VISIBLE
            viewHolder.manyShipsTeamB.text = manyShipsTeamB.toString()
        } else {
            viewHolder.planetHasShipImgTeamB.visibility = GONE
            viewHolder.manyShipsTeamB.visibility = GONE
        }

        

        var manyUnitsTeamA = 0
        var manyUnitsTeamB = 0
        planetWithUnit.units.forEach { unit ->
            when(unit.team) {
                TeamLoyalty.TeamA -> manyUnitsTeamA = manyUnitsTeamA.inc()
                TeamLoyalty.TeamB -> manyUnitsTeamB = manyUnitsTeamB.inc()
                else -> {}
            }
        }
        if(manyUnitsTeamA>0) {
            viewHolder.planetHasUnitsImgTeamA.visibility = VISIBLE
            viewHolder.manyUnitsTeamA.visibility = VISIBLE
            viewHolder.manyUnitsTeamA.text = manyUnitsTeamA.toString()
        } else {
            viewHolder.planetHasUnitsImgTeamA.visibility = GONE
            viewHolder.manyUnitsTeamA.visibility = GONE
        }

        if(manyUnitsTeamB>0) {
            viewHolder.planetHasUnitsImgTeamB.visibility = VISIBLE
            viewHolder.manyUnitsTeamB.visibility = VISIBLE
            viewHolder.manyUnitsTeamB.text = manyUnitsTeamB.toString()
        } else {
            viewHolder.planetHasUnitsImgTeamB.visibility = GONE
            viewHolder.manyUnitsTeamB.visibility = GONE
        }

        

        if(planetWithUnit.defenseStructures.isEmpty()) {
            viewHolder.planetHasDefenseImg.visibility = GONE
            viewHolder.manyDefense.visibility = GONE
        } else {
            viewHolder.planetHasDefenseImg.visibility = VISIBLE
            viewHolder.manyDefense.text = planetWithUnit.defenseStructures.size.toString()
        }


        if(planetWithUnit.factories.isEmpty()) {
            viewHolder.planetHasFactoriesImg.visibility = GONE
            viewHolder.manyFactories.visibility = GONE
        } else {
            viewHolder.planetHasFactoriesImg.visibility = VISIBLE
            viewHolder.manyFactories.text = planetWithUnit.factories.size.toString()
        }

        Utilities.populateEnergiesUi(viewHolder.itemView.context, viewHolder.energyList, planetWithUnit)

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
