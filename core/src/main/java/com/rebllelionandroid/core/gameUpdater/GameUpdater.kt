package com.rebllelionandroid.core.gameUpdater

import com.rebllelionandroid.core.GameStateViewModel

class GameUpdater {

    companion object {
        fun updateGameState(
            gameStateViewModel: GameStateViewModel,
            gameStateId: Long
        ) {
            val gameStateWithSectors = gameStateViewModel.getGameStateWithSectors(gameStateId)
            val timeVal = gameStateWithSectors.gameState.gameTime.plus(1)
            timeVal.let { gameStateViewModel.updateGameTime(gameStateId, it) }
            println("New time day:$timeVal")

            val sectorsWithPlanets = gameStateWithSectors.sectors
            for(sectorWithPlanets in sectorsWithPlanets) {
                for(planetWithUnits in sectorWithPlanets.planets)  {
                    val planet = planetWithUnits.planet
                    var teamALoyalty = planetWithUnits.planet.teamALoyalty
                    if(teamALoyalty>90) {
                        teamALoyalty = 0
                    } else {
                        teamALoyalty+=10
                    }
                    gameStateViewModel.updatePlanetLoyalty(planet.id, teamALoyalty)
                }
            }

        }
    }
}