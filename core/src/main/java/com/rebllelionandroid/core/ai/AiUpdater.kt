package com.rebllelionandroid.core.ai

import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.commands.CommandUtilities
import com.rebllelionandroid.core.database.gamestate.Personnel

class AiUpdater {
    companion object {
        /** Perform all read and write DB operations through the gameStateViewModel.
        * Make sure all write operations are synchronous and complete before this method
        * returns
        */
        fun update(
            gameStateViewModel: GameStateViewModel,
            currentGameStateId: Long,
        ) {
            val gameStateWithSectors = gameStateViewModel.getGameStateWithSectors(currentGameStateId)

            // This is a dumb example of how to simply move units back-and-forth from the planet
            //  surface up to the ships in orbit, and back down to the planet surface
            gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
                // planets
                sectorWithPlanets.planets.forEach { planetWithUnits ->
                    if(planetWithUnits.shipsWithUnits.isNotEmpty()) {
                        val personnelToMoveToShip = mutableListOf<Personnel>()
                        val personnelToMoveToPlanet = mutableListOf<Personnel>()

                        personnelToMoveToShip.addAll(planetWithUnits.personnels)
                        planetWithUnits.shipsWithUnits.forEach { shipWithUnits ->
                            personnelToMoveToPlanet.addAll(shipWithUnits.personnels)
                        }

                        val dstShipWithUnits = planetWithUnits.shipsWithUnits.random()
                        personnelToMoveToShip.forEach { personnel ->
                            CommandUtilities.moveUnitToShip(
                                gameStateViewModel,
                                personnel.id,
                                dstShipWithUnits.ship.id,
                                currentGameStateId
                            )
                        }
                        personnelToMoveToPlanet.forEach { personnel ->
                            CommandUtilities.moveUnitToPlanetSurface(
                                gameStateViewModel,
                                personnel.id,
                                planetWithUnits.planet.id,
                                currentGameStateId
                            )
                        }
                    }
                }
            }// for each sector
        }
    }
}