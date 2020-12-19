package com.rebllelionandroid.core.gameUpdater

import com.rebllelionandroid.core.GameStateViewModel

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

            // *** Update Battles ***
            gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
                // planets
                sectorWithPlanets.planets.forEach { planetWithUnits ->
                    val planet = planetWithUnits.planet
                    // ships
                    planetWithUnits.shipsWithUnits.forEach { shipWithUnits ->
                    }// ships

                    // units on surface
                    planetWithUnits.units.forEach { unit ->

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

            return updateEvents
        }
    }
}
