package com.rebllelionandroid.core

import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.rebllelionandroid.core.database.gamestate.*
import com.rebllelionandroid.core.database.gamestate.Personnel
import com.rebllelionandroid.core.database.gamestate.enums.FactoryBuildTargetType
import com.rebllelionandroid.core.database.gamestate.enums.ShipType
import com.rebllelionandroid.core.database.gamestate.enums.UnitType
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

        fun getPlanetEnergiesFull(planetWithUnit: PlanetWithUnits): Int {
            val manyFactories = planetWithUnit.factories.size
            val manyDefStructure = planetWithUnit.defenseStructures.size
            val manyEnergiesFull = manyFactories + manyDefStructure
            return manyEnergiesFull
        }

        fun getPlanetEnergiesEmpty(planetWithUnit: PlanetWithUnits): Int {
            val manyFactories = planetWithUnit.factories.size
            val manyDefStructure = planetWithUnit.defenseStructures.size
            val manyEnergiesFull = manyFactories + manyDefStructure
            val manyEnergiesEmpty = planetWithUnit.planet.energyCap - manyEnergiesFull
            return manyEnergiesEmpty
        }

        fun populateEnergiesUi(
            context: Context,
            energyList: LinearLayout,
            planetWithUnit: PlanetWithUnits
        ) {
            val manyEnergiesFull = getPlanetEnergiesFull(planetWithUnit)
            val manyEnergiesEmpty = getPlanetEnergiesEmpty(planetWithUnit)

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

        fun findPlanetWithId(gameStateWithSectors: GameStateWithSectors, planetId: Long): PlanetWithUnits? {
            var respPlanetWithUnits: PlanetWithUnits? = null
            gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
                sectorWithPlanets.planets.forEach { planetWithUnits ->
                    if(planetWithUnits.planet.id == planetId)
                        respPlanetWithUnits = planetWithUnits
                }
            }
            return respPlanetWithUnits
        }

        fun getTravelArrivalDay(srcPlanet: Planet, dstPlanet: Planet, gameTime: Long): Long {
            val tripDurationDays = Math.abs(srcPlanet.locationIndex - dstPlanet.locationIndex)
            return gameTime + tripDurationDays
        }

        fun setShipStrengthValues(ship: Ship) {
            when(ship.shipType) {
                ShipType.Bireme -> {
                    ship.attackStrength = 2
                    ship.defenseStrength = 2
                    ship.healthPoints = 2
                    ship.unitCapacity = 2
                }
                ShipType.Trireme -> {
                    ship.attackStrength = 3
                    ship.defenseStrength = 3
                    ship.healthPoints = 3
                    ship.unitCapacity = 3
                }
                ShipType.Quadrireme -> {
                    ship.attackStrength = 4
                    ship.defenseStrength = 4
                    ship.healthPoints = 4
                    ship.unitCapacity = 4
                }
                ShipType.Quinquereme -> {
                    ship.attackStrength = 5
                    ship.defenseStrength = 5
                    ship.healthPoints = 5
                    ship.unitCapacity = 5
                }
                ShipType.Hexareme -> {
                    ship.attackStrength = 6
                    ship.defenseStrength = 6
                    ship.healthPoints = 6
                    ship.unitCapacity = 6
                }
                ShipType.Septireme -> {
                    ship.attackStrength = 7
                    ship.defenseStrength = 7
                    ship.healthPoints = 7
                    ship.unitCapacity = 7
                }
                ShipType.Octere -> {
                    ship.attackStrength = 8
                    ship.defenseStrength = 8
                    ship.healthPoints = 8
                    ship.unitCapacity = 8
                }
            }
        }

        fun setUnitStrengthValues(personnel: Personnel) {
            when(personnel.unitType) {
                UnitType.Garrison -> {
                    personnel.attackStrength = 10
                    personnel.defenseStrength = 10
                    personnel.healthPoints = 10
                }
                UnitType.SpecialForces -> {
                    personnel.attackStrength = 2
                    personnel.defenseStrength = 2
                    personnel.healthPoints = 2
                }
            }
        }

        fun getCurrentGameStateId(
            gameStateSharedPrefFile: String,
            keyCurrentGameId: String,
            contextWrapper: ContextWrapper
        ): Long? {
            val sharedPref = contextWrapper?.getSharedPreferences(
                gameStateSharedPrefFile,
                Context.MODE_PRIVATE
            )
            if(sharedPref?.contains(keyCurrentGameId) == true) {
                return sharedPref.getLong(keyCurrentGameId, 0)
            } else {
                println("OrdersDialogFragment.onResume ERROR No current game ID found in shared preferences")
                return null
            }
        }

        fun isBuildOrderForStructure(factoryBuildTargetType: FactoryBuildTargetType): Boolean {
            return (
                factoryBuildTargetType==FactoryBuildTargetType.ConstructionYard_ConstructionYard ||
                factoryBuildTargetType==FactoryBuildTargetType.ConstructionYard_ShipYard ||
                factoryBuildTargetType==FactoryBuildTargetType.ConstructionYard_TrainingFacility ||
                factoryBuildTargetType==FactoryBuildTargetType.ConstructionYard_OrbitalBattery ||
                factoryBuildTargetType==FactoryBuildTargetType.ConstructionYard_PlanetaryShield
                )
        }
    }
}
