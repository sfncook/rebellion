package com.rebllelionandroid.core.gameUpdater

import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.Planet
import com.rebllelionandroid.core.database.gamestate.Ship
import com.rebllelionandroid.core.database.gamestate.ShipWithUnits
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import com.rebllelionandroid.core.gameUpdater.events.*
import kotlin.random.Random

class GameUpdater {

    companion object {
        fun updateGameState(
            gameStateViewModel: GameStateViewModel,
            gameStateId: Long
        ): List<UpdateEvent> {
            val updateEvents = mutableListOf<UpdateEvent>()

            var gameStateWithSectors = gameStateViewModel.getGameStateWithSectors(gameStateId)
            var gameStateWithSectors2 = gameStateWithSectors.deepCopy()

            val timeDay = gameStateWithSectors.gameState.gameTime.plus(1)
            timeDay.let { gameStateViewModel.updateGameTime(gameStateId, it) }

            // *** Update Conflict Results ***
            gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
                // planets
                sectorWithPlanets.planets.forEach { planetWithUnits ->
                    val planet = planetWithUnits.planet

                    if(planet.inConflict) {
                        // ships
                        val teamsToShips = Utilities.getTeamsToShipsForList(planetWithUnits.shipsWithUnits)

                        val atkStrengthTeamA: Int? = teamsToShips[TeamLoyalty.TeamA]?.sumBy { it.ship.attackStrength }
                        val defStrengthTeamA: Int? = teamsToShips[TeamLoyalty.TeamA]?.sumBy { it.ship.defenseStrength }

                        val atkStrengthTeamB: Int? = teamsToShips[TeamLoyalty.TeamB]?.sumBy { it.ship.attackStrength }
                        val defStrengthTeamB: Int? = teamsToShips[TeamLoyalty.TeamB]?.sumBy { it.ship.defenseStrength }

                        val attackPointsTeamA: Int = Random.nextInt(0, atkStrengthTeamA!!) - defStrengthTeamB!!
                        val attackPointsTeamB: Int = Random.nextInt(0, atkStrengthTeamB!!) - defStrengthTeamA!!

                        applyDamage(
                            _attackPoints = attackPointsTeamA,
                            defenseShipList = teamsToShips[TeamLoyalty.TeamB] ?: error("Null teamToShips for TeamB"),
                            gameStateViewModel,
                            planet,
                            updateEvents
                        )

                        applyDamage(
                            _attackPoints = attackPointsTeamB,
                            defenseShipList = teamsToShips[TeamLoyalty.TeamA] ?: error("Null teamToShips for TeamA"),
                            gameStateViewModel,
                            planet,
                            updateEvents
                        )



                        // units on surface
                        planetWithUnits.units.forEach { unit ->

                        }
                    }
                }// planets
            }// sectors


            // *** Update Loyalties ***
            gameStateWithSectors = gameStateViewModel.getGameStateWithSectors(gameStateId)
            gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
                // planets
                sectorWithPlanets.planets.forEach { planetWithUnits ->
                    val planet = planetWithUnits.planet
                }// planets
            }// sectors


            // *** Update Movement ***
            gameStateWithSectors = gameStateViewModel.getGameStateWithSectors(gameStateId)
            gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
                // planets
                sectorWithPlanets.planets.forEach { planetWithUnits ->
                    val planet = planetWithUnits.planet
                    // ships
                    planetWithUnits.shipsWithUnits.forEach { shipWithUnits ->
                        val ship = shipWithUnits.ship
                        if(ship.isTraveling && timeDay >= ship.dayArrival ) {
                            gameStateViewModel.endShipJourney(ship.id)
                            updateEvents.add(ShipArrivalEvent(ship, planet))
                        }
                    }// ships

                    // units on surface
                    planetWithUnits.units.forEach { unit ->

                    }
                }// planets
            }// sectors

            // *** Check for conflict flags ***
            gameStateWithSectors = gameStateViewModel.getGameStateWithSectors(gameStateId)
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
                        gameStateViewModel.setPlanetInConflict(planet.id, true)
                        if(!planet.inConflict) {
                            updateEvents.add(PlanetConflictStartsEvent(planet))
                        } else {
                            updateEvents.add(PlanetConflictContinuesEvent(planet))
                        }
                    } else {
                        gameStateViewModel.setPlanetInConflict(planet.id, false)
                    }

                    // units on surface
                    planetWithUnits.units.forEach { unit ->

                    }
                }// planets
            }// sectors

            // Cleanup
            gameStateViewModel.deleteAllDestroyedShips()

            return updateEvents
        }// updateGameState

        private fun applyDamage(
            _attackPoints: Int,
            defenseShipList: List<ShipWithUnits>,
            gameStateViewModel: GameStateViewModel,
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
                            gameStateViewModel.setShipDestroyed(
                                ship.id,
                                true
                            )
                            updateEvents.add(ShipDestroyedEvent(ship, planet))
                        }
                        gameStateViewModel.setShipHealthPoints(
                            ship.id,
                            newHealthPoints
                        )
                    }
                }
            }
        }// applyDamage
    }// component
}
