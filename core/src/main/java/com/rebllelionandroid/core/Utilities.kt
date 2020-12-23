package com.rebllelionandroid.core

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
    }
}
