package com.rebllelionandroid.core.gameUpdater

import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.Planet
import com.rebllelionandroid.core.database.gamestate.Ship
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

            val gameStateWithSectors = gameStateViewModel.getGameStateWithSectors(gameStateId)
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
                            attackedShip = teamsToShips[TeamLoyalty.TeamB]?.random()?.ship!!,
                            gameStateViewModel,
                            planet,
                            updateEvents
                        )

                        applyDamage(
                            _attackPoints = attackPointsTeamB,
                            attackedShip = teamsToShips[TeamLoyalty.TeamA]?.random()?.ship!!,
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

        fun applyDamage(
            _attackPoints: Int,
            attackedShip: Ship,
            gameStateViewModel: GameStateViewModel,
            planet: Planet,
            updateEvents: MutableList<UpdateEvent>
        ) {
            var attackPoints = _attackPoints
            while(attackPoints > 0) {
                if(attackPoints> attackedShip.defenseStrength) {
                    if(!attackedShip.destroyed) {
                        gameStateViewModel.setShipDestroyed(
                            attackedShip.id,
                            true
                        )
                        updateEvents.add(ShipDestroyedEvent(attackedShip, planet))
                    }
                }
                attackPoints = attackPoints.minus(attackedShip.defenseStrength)
            }
        }
    }
}
