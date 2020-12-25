package com.rebllelionandroid.core

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.rebllelionandroid.core.database.gamestate.*
import com.rebllelionandroid.core.database.gamestate.Unit
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty

class Utilities {
    companion object {
        val LOYALTY_LIMIT = 50
        val NEUTRALITY_LIMIT = 10

        fun getLoyaltyIconForPlanet(planet: Planet): Pair<Int, Int> {
            val loyaltyDiff = planet.teamALoyalty - planet.teamBLoyalty
            val imgId: Int
            val colorId: Int
            /*if(!planet.isExplored) {
                imgId = R.drawable.loyalty_sm
                colorId = R.color.loyalty_unexplored
            } else*/ if( loyaltyDiff > LOYALTY_LIMIT ) {
                imgId = R.drawable.loyalty_lg
                colorId = R.color.loyalty_team_a
            } else if( loyaltyDiff > NEUTRALITY_LIMIT) {
                imgId = R.drawable.loyalty_sm
                colorId = R.color.loyalty_team_a
            } else if( loyaltyDiff < -LOYALTY_LIMIT ) {
                imgId = R.drawable.loyalty_lg
                colorId = R.color.loyalty_team_b
            } else if( loyaltyDiff < -NEUTRALITY_LIMIT ) {
                imgId = R.drawable.loyalty_sm
                colorId = R.color.loyalty_team_b
            } else {
                imgId = R.drawable.loyalty_lg
                colorId = R.color.loyalty_neutral
            }
            return Pair(imgId, colorId)
        }

        fun getPlanetLoyalty(planet: Planet): TeamLoyalty {
            val loyaltyDiff = planet.teamALoyalty - planet.teamBLoyalty
            if( loyaltyDiff > LOYALTY_LIMIT ) {
                return TeamLoyalty.TeamA
            } else if( loyaltyDiff > NEUTRALITY_LIMIT) {
                return TeamLoyalty.TeamA
            } else if( loyaltyDiff < -LOYALTY_LIMIT ) {
                return TeamLoyalty.TeamB
            } else if( loyaltyDiff < -NEUTRALITY_LIMIT ) {
                return TeamLoyalty.TeamB
            } else {
                return TeamLoyalty.Neutral
            }
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

        fun populateShipsInSectorUi(
            sectorWithPlanets: SectorWithPlanets,
            manyShipsInSectorTxt: TextView,
            shipsInSectorImg: ImageView,
            manyEnemyShipsInSectorTxt: TextView,
            enemyShipsInSectorImg: ImageView
        ) {
            var manyMyShips = 0
            var manyEnemyShips = 0
            sectorWithPlanets.planets.forEach { planetWithUnits ->
                val teamsToShips = getTeamsToShipsForList(planetWithUnits.shipsWithUnits)
                manyMyShips = manyMyShips.plus(teamsToShips[TeamLoyalty.TeamA]?.size ?: 0)
                manyEnemyShips = manyEnemyShips.plus(teamsToShips[TeamLoyalty.TeamB]?.size ?: 0)
            }

            populateShipsUi(
                manyMyShips,
                manyEnemyShips,
                manyShipsInSectorTxt,
                shipsInSectorImg,
                manyEnemyShipsInSectorTxt,
                enemyShipsInSectorImg
            )
        }

        fun populateShipsAtPlanetUi(
            planetWithUnits: PlanetWithUnits,
            manyShipsInSectorTxt: TextView,
            shipsInSectorImg: ImageView,
            manyEnemyShipsInSectorTxt: TextView,
            enemyShipsInSectorImg: ImageView
        ) {
            val teamsToShips = getTeamsToShipsForList(planetWithUnits.shipsWithUnits)
            val manyMyShips = teamsToShips[TeamLoyalty.TeamA]?.size ?: 0
            val manyEnemyShips = teamsToShips[TeamLoyalty.TeamB]?.size ?: 0

            populateShipsUi(
                manyMyShips,
                manyEnemyShips,
                manyShipsInSectorTxt,
                shipsInSectorImg,
                manyEnemyShipsInSectorTxt,
                enemyShipsInSectorImg
            )
        }

        private fun populateShipsUi(
            manyMyShips: Int,
            manyEnemyShips: Int,
            manyShipsInSectorTxt: TextView,
            shipsInSectorImg: ImageView,
            manyEnemyShipsInSectorTxt: TextView,
            enemyShipsInSectorImg: ImageView
        ) {
            if(manyMyShips>0) {
                manyShipsInSectorTxt.visibility = View.VISIBLE
                shipsInSectorImg.visibility = View.VISIBLE
                manyShipsInSectorTxt.text = manyMyShips.toString()
                shipsInSectorImg.setColorFilter(
                    ContextCompat.getColor(shipsInSectorImg.context, R.color.loyalty_team_a),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
            } else {
                manyShipsInSectorTxt.visibility = View.GONE
                shipsInSectorImg.visibility = View.GONE
            }

            if(manyEnemyShips>0) {
                manyEnemyShipsInSectorTxt.visibility = View.VISIBLE
                enemyShipsInSectorImg.visibility = View.VISIBLE
                manyEnemyShipsInSectorTxt.text = manyEnemyShips.toString()
                enemyShipsInSectorImg.setColorFilter(
                    ContextCompat.getColor(enemyShipsInSectorImg.context, R.color.loyalty_team_b),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
            } else {
                manyEnemyShipsInSectorTxt.visibility = View.GONE
                enemyShipsInSectorImg.visibility = View.GONE
            }
        }
    }
}
