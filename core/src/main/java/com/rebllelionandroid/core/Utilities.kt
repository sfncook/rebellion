package com.rebllelionandroid.core

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.rebllelionandroid.core.database.gamestate.Planet
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.gamestate.ShipWithUnits
import com.rebllelionandroid.core.database.gamestate.Unit
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty

class Utilities {
    companion object {
        fun getLoyaltyIconForPlanet(planet: Planet): Pair<Int, Int> {
            val loyaltyDiff = planet.teamALoyalty - planet.teamBLoyalty
            val imgId: Int
            val colorId: Int
            /*if(!planet.isExplored) {
                imgId = R.drawable.loyalty_sm
                colorId = R.color.loyalty_unexplored
            } else*/ if( 40 < loyaltyDiff) {
                imgId = R.drawable.loyalty_lg
                colorId = R.color.loyalty_team_a
            } else if( 10 < loyaltyDiff) {
                imgId = R.drawable.loyalty_sm
                colorId = R.color.loyalty_team_a
            } else if( -40 > loyaltyDiff) {
                imgId = R.drawable.loyalty_lg
                colorId = R.color.loyalty_team_b
            } else if( -10 > loyaltyDiff) {
                imgId = R.drawable.loyalty_sm
                colorId = R.color.loyalty_team_b
            } else {
                imgId = R.drawable.loyalty_lg
                colorId = R.color.loyalty_neutral
            }
            return Pair(imgId, colorId)
        }

        fun sortPlanets(planetsWithUnits: List<PlanetWithUnits>): List<PlanetWithUnits> {
            val sortedPlanets = planetsWithUnits.toSortedSet(Comparator { p1, p2 ->
                p1.planet.name.compareTo(p2.planet.name)
            })
            return ArrayList(sortedPlanets)
        }

        fun getTeamsToShipsForList(shipsWithUnits: List<ShipWithUnits>): Map<TeamLoyalty, List<ShipWithUnits>> {
            return shipsWithUnits.groupBy { it.ship.team }
        }

        fun getTeamsToUnitsOnPlanet(planetsWithUnits: PlanetWithUnits): Map<TeamLoyalty, List<Unit>> {
            return planetsWithUnits.units.groupBy { it.team }
        }

        fun populateEnergiesUi(
            context: Context,
            energyList: LinearLayout,
            manyEnergiesFull: Int,
            manyEnergiesEmpty: Int
        ) {
            energyList.removeAllViews()
            val layoutParams = LinearLayout.LayoutParams(30, 30)
            layoutParams.setMargins(3, 3, 3, 3)
            for (eIndex in 1 .. manyEnergiesFull) {
                val imgView = ImageView(context)
                imgView.layoutParams = layoutParams
                imgView.setImageResource(R.drawable.energy_used)
                imgView.setColorFilter(
                    ContextCompat.getColor(context, R.color.black),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
                energyList.addView(imgView)
            }
            for (eIndex in 1 .. manyEnergiesEmpty) {
                val imgView = ImageView(context)
                imgView.layoutParams = layoutParams
                imgView.paddingTop
                imgView.setImageResource(R.drawable.energy_empty)
                imgView.setColorFilter(
                    ContextCompat.getColor(context, R.color.black),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
                energyList.addView(imgView)
            }
        }
    }
}
