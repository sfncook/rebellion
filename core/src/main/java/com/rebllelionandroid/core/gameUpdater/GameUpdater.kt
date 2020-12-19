package com.rebllelionandroid.core.gameUpdater

import com.rebllelionandroid.core.GameStateViewModel

class GameUpdater {

    companion object {
        fun updateGameState(
            gameStateViewModel: GameStateViewModel,
            gameStateId: Long
        ) {
            val gameStateWithSectors = gameStateViewModel.getGameStateWithSectors(gameStateId)
            val timeDay = gameStateWithSectors.gameState.gameTime.plus(1)
            timeDay.let { gameStateViewModel.updateGameTime(gameStateId, it) }
            println("New time day:$timeDay")

//            val sectorsWithPlanets = gameStateWithSectors.sectors
//            for(sectorWithPlanets in sectorsWithPlanets) {
//                for(planetWithUnits in sectorWithPlanets.planets)  {
//                    val planet = planetWithUnits.planet
//                    var teamALoyalty = planetWithUnits.planet.teamALoyalty
//                    if(teamALoyalty>90) {
//                        teamALoyalty = 0
//                    } else {
//                        teamALoyalty+=10
//                    }
//                    gameStateViewModel.updatePlanetLoyalty(planet.id, teamALoyalty)
//                }
//            }

            // sectors
            gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
                // planets
                sectorWithPlanets.planets.forEach { planetWithUnits ->
                    // ships
                    planetWithUnits.shipsWithUnits.forEach { shipWithUnits ->
                        val ship = shipWithUnits.ship
                        if(ship.isTraveling && timeDay >= ship.dayArrival ) {
                            gameStateViewModel.endShipJourney(ship.id)
                        }
                    }// ships

                    // units on surface
                    planetWithUnits.units.forEach { unit ->

                    }
                }// planets
            }// sectors

        }
    }
}