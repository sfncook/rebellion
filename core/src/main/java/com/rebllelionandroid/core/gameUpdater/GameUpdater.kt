package com.rebllelionandroid.core.gameUpdater

import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.GameStateWithSectors
import com.rebllelionandroid.core.database.gamestate.Planet
import com.rebllelionandroid.core.database.gamestate.Ship
import com.rebllelionandroid.core.database.gamestate.ShipWithUnits
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import com.rebllelionandroid.core.gameUpdater.events.*
import kotlin.random.Random

class GameUpdater {

    companion object {
        fun updateGameState(gameStateWithSectors: GameStateWithSectors): Pair<GameStateWithSectors, List<UpdateEvent>> {
            val updateEvents = mutableListOf<UpdateEvent>()

            gameStateWithSectors.gameState.gameTime = gameStateWithSectors.gameState.gameTime.plus(1)
            val timeDay = gameStateWithSectors.gameState.gameTime

            // *** Update Conflict Results ***
            gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
                // planets
                sectorWithPlanets.planets.forEach { planetWithUnits ->
                    val planet = planetWithUnits.planet

                    if(planet.inConflict) {
                        // ships
                        val teamsToShips = Utilities.getTeamsToShipsForList(planetWithUnits.shipsWithUnits)
                        applyDamage(teamsToShips[TeamLoyalty.TeamA]!!, teamsToShips[TeamLoyalty.TeamB]!!)
                        applyDamage(teamsToShips[TeamLoyalty.TeamB]!!, teamsToShips[TeamLoyalty.TeamA]!!)

//                        val atkStrengthTeamA: Int? = teamsToShips[TeamLoyalty.TeamA]?.sumBy { it.ship.attackStrength }
//                        val defStrengthTeamA: Int? = teamsToShips[TeamLoyalty.TeamA]?.sumBy { it.ship.defenseStrength }
//
//                        val atkStrengthTeamB: Int? = teamsToShips[TeamLoyalty.TeamB]?.sumBy { it.ship.attackStrength }
//                        val defStrengthTeamB: Int? = teamsToShips[TeamLoyalty.TeamB]?.sumBy { it.ship.defenseStrength }
//
//                        val attackPointsTeamA: Int = Random.nextInt(0, atkStrengthTeamA!!) - defStrengthTeamB!!
//                        val attackPointsTeamB: Int = Random.nextInt(0, atkStrengthTeamB!!) - defStrengthTeamA!!
//
//                        applyDamage(
//                            _attackPoints = attackPointsTeamA,
//                            defenseShipList = teamsToShips[TeamLoyalty.TeamB] ?: error("Null teamToShips for TeamB"),
//                            planet,
//                            updateEvents
//                        )
//
//                        applyDamage(
//                            _attackPoints = attackPointsTeamB,
//                            defenseShipList = teamsToShips[TeamLoyalty.TeamA] ?: error("Null teamToShips for TeamA"),
//                            planet,
//                            updateEvents
//                        )



                        // units on surface
                        planetWithUnits.units.forEach { unit ->

                        }
                    }
                }// planets
            }// sectors


            // *** Update Loyalties ***
            gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
                // planets
                sectorWithPlanets.planets.forEach { planetWithUnits ->
                    val planet = planetWithUnits.planet
                }// planets
            }// sectors


            // *** Update Movement ***
            gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
                // planets
                sectorWithPlanets.planets.forEach { planetWithUnits ->
                    val planet = planetWithUnits.planet
                    // ships
                    planetWithUnits.shipsWithUnits.forEach { shipWithUnits ->
                        val ship = shipWithUnits.ship
                        if(ship.isTraveling && timeDay >= ship.dayArrival ) {
                            ship.isTraveling = false
                            ship.dayArrival = 0
                            ship.updated = true
                            updateEvents.add(ShipArrivalEvent(ship, planet))
                        }
                    }// ships

                    // units on surface
                    planetWithUnits.units.forEach { unit ->

                    }
                }// planets
            }// sectors

            // *** Check for conflict flags ***
            gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
                // planets
                sectorWithPlanets.planets.forEach { planetWithUnits ->
                    val planet = planetWithUnits.planet

                    // ships
                    val teamsToShips = Utilities.getTeamsToShipsForList(planetWithUnits.shipsWithUnits)
                    if(
                        teamsToShips[TeamLoyalty.TeamA]?.any { !it.ship.isTraveling && !it.ship.destroyed } == true &&
                        teamsToShips[TeamLoyalty.TeamB]?.any { !it.ship.isTraveling && !it.ship.destroyed } == true
                    ) {
                        planet.inConflict = true
                        planet.updated = true
                        if(!planet.inConflict) {
                            updateEvents.add(PlanetConflictStartsEvent(planet))
                        } else {
                            updateEvents.add(PlanetConflictContinuesEvent(planet))
                        }
                    } else {
                        planet.inConflict = false
                        planet.updated = true
                    }

                    // units on surface
                    planetWithUnits.units.forEach { unit ->

                    }
                }// planets
            }// sectors

            return Pair(gameStateWithSectors, updateEvents)
        }// updateGameState

        private fun applyDamage(
            offensiveShips: List<ShipWithUnits>,
            defensiveShips: List<ShipWithUnits>
        ) {
            offensiveShips.forEach { shipWithUnits ->
                val ofShip = shipWithUnits.ship
                if(Random.nextBoolean()) {
                    val defShipWithUnits = defensiveShips.random()
                    val defShip = defShipWithUnits.ship
                    val attackPoints = Random.nextInt(1, ofShip.attackStrength)
                    defShip.healthPoints = defShip.healthPoints - attackPoints
                    defShip.healthPoints.coerceAtLeast(0)
                }
            }
        }

        private fun applyDamage(
            _attackPoints: Int,
            defenseShipList: List<ShipWithUnits>,
            planet: Planet,
            updateEvents: MutableList<UpdateEvent>
        ) {
            var attackPoints = _attackPoints
            val updatedShips = mutableSetOf<Ship>()
            val shipsToHealthPoints =
                defenseShipList.map { it.ship }.associateBy({ it }, { it.healthPoints }).toMutableMap()
            while(attackPoints > 0 && shipsToHealthPoints.filterValues { it > 0 }.any()) {
                val attackedShipWithUnits = defenseShipList.random()
                val attackedShip = attackedShipWithUnits.ship
                val attackedShipHealthPoints = shipsToHealthPoints[attackedShip]
                if(attackedShipHealthPoints!=null) {
                    if (attackPoints > attackedShip.healthPoints) {
                        attackPoints = attackPoints.minus(attackedShip.healthPoints)
                        shipsToHealthPoints[attackedShip] = 0
                    } else {
                        shipsToHealthPoints[attackedShip] = attackedShipHealthPoints - attackPoints
                        attackPoints = attackPoints.minus(0)
                    }
                    updatedShips.add(attackedShip)
                }
            }

            updatedShips.forEach { ship ->
                shipsToHealthPoints[ship]?.let { newHealthPoints ->
                    run {
                        if (newHealthPoints <= 0) {
                            ship.destroyed = true
                            updateEvents.add(ShipDestroyedEvent(ship, planet))
                        }
                        ship.healthPoints = newHealthPoints
                        ship.updated = true
                    }
                }
            }
        }// applyDamage
    }// component
}
