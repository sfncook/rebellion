package com.rebllelionandroid.core.gameUpdater

import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.Factory
import com.rebllelionandroid.core.database.gamestate.GameStateWithSectors
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.gamestate.ShipWithUnits
import com.rebllelionandroid.core.database.gamestate.enums.FactoryBuildTargetType
import com.rebllelionandroid.core.database.gamestate.enums.FactoryType
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import com.rebllelionandroid.core.gameUpdater.events.*
import com.rebllelionandroid.core.gameUpdater.uprising.UprisingEval
import kotlin.random.Random

class GameUpdater {

    companion object {
        fun updateGameState(gameStateWithSectors: GameStateWithSectors): Pair<GameStateWithSectors, List<UpdateEvent>> {
            val updateEvents = mutableListOf<UpdateEvent>()

            gameStateWithSectors.gameState.gameTime = gameStateWithSectors.gameState.gameTime.plus(1)
            val gameTime = gameStateWithSectors.gameState.gameTime

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

                    val uprisingRank = UprisingEval.getUprisingRank(
                        planet.teamALoyalty,
                        planetWithUnits.units.size
                    )
                }// planets
            }// sectors


            updateEntityMovement(gameStateWithSectors, updateEvents)


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

            updateFactoryBuildOrders(gameStateWithSectors, updateEvents)

            return Pair(gameStateWithSectors, updateEvents)
        }// updateGameState

        private fun applyDamage(offensiveShips: List<ShipWithUnits>, defensiveShips: List<ShipWithUnits>) {
            offensiveShips.forEach { shipWithUnits ->
                val ofShip = shipWithUnits.ship
                if(Random.nextBoolean()) {
                    val defShipWithUnits = defensiveShips.random()
                    val defShip = defShipWithUnits.ship
                    val attackPoints = Random.nextInt(1, ofShip.attackStrength)
                    defShip.healthPoints = defShip.healthPoints - attackPoints
                    println("Ship takes damage: ${defShip.team} damage:${attackPoints} healthPoints:${defShip.healthPoints}")
                    if(defShip.healthPoints<=0) defShip.destroyed = true
                    defShip.updated = true
                }
            }
        }

        private fun updateEntityMovement(gameStateWithSectors: GameStateWithSectors, updateEvents: MutableList<UpdateEvent>) {
            val gameTime = gameStateWithSectors.gameState.gameTime
            gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
                // planets
                sectorWithPlanets.planets.forEach { planetWithUnits ->
                    val planet = planetWithUnits.planet

                    planetWithUnits.shipsWithUnits.forEach { shipWithUnits ->
                        val ship = shipWithUnits.ship
                        if(ship.isTraveling && gameTime >= ship.dayArrival ) {
                            ship.isTraveling = false
                            ship.dayArrival = 0
                            ship.updated = true
                            updateEvents.add(ShipArrivalEvent(ship, planet))
                        }
                    }

                    planetWithUnits.factories.forEach { factory ->
                        if(factory.isTravelling && gameTime >= factory.dayArrival ) {
                            factory.isTravelling = false
                            factory.dayArrival = 0
                            factory.updated = true
                            updateEvents.add(FactoryArrivalEvent(factory, planetWithUnits.planet))
                        }
                    }
                }// planets
            }// sectors
        }

        private fun updateFactoryBuildOrders(gameStateWithSectors: GameStateWithSectors, updateEvents: MutableList<UpdateEvent>) {
            val gameTime = gameStateWithSectors.gameState.gameTime
            gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
                // planets
                sectorWithPlanets.planets.forEach { planetWithUnits ->
                    planetWithUnits.factories.forEach { factory ->
                        if(factory.buildTargetType != null && gameTime >= factory.dayBuildComplete) {
                            when(factory.buildTargetType) {
                                FactoryBuildTargetType.ConstructionYard_ConstructionYard -> createNewFactory(planetWithUnits, FactoryType.ConstructionYard)
                            }
                        }
                    }
                }// planets
            }// sectors
        }

        private fun createNewFactory(planetWithUnits: PlanetWithUnits, factoryType: FactoryType, updateEvents: MutableList<UpdateEvent>) {
        }
    }// component
}
