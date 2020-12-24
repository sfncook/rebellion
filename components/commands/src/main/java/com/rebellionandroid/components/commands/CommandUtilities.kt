package com.rebellionandroid.components.commands

import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty

class CommandUtilities {
    companion object {
        private const val PLANET_LOYALTY_THRESHOLD = 50

        fun moveUnitToPlanetSurface(
            gameStateViewModel: GameStateViewModel,
            unitId: Long,
            destPlanetId: Long,
            currentGameStateId: Long
        ) {
            gameStateViewModel.getPlanet(destPlanetId) { planet ->
                gameStateViewModel.getUnit(unitId) { unit ->
                    var newTeamALoyalty = planet.teamALoyalty
                    var newTeamBLoyalty = planet.teamBLoyalty
                    if(unit.team == TeamLoyalty.TeamA) {
                        if(planet.teamALoyalty>= PLANET_LOYALTY_THRESHOLD) {
                            newTeamALoyalty += 2
                            newTeamBLoyalty -= 1
                        } else {
                            newTeamALoyalty -= 1
                        }
                    } else {
                        if(planet.teamALoyalty>= PLANET_LOYALTY_THRESHOLD) {
                            newTeamBLoyalty += 2
                            newTeamALoyalty -= 1
                        } else {
                            newTeamBLoyalty -= 1
                        }
                    }
                    gameStateViewModel.moveUnitToPlanet(unitId, destPlanetId, currentGameStateId, newTeamALoyalty, newTeamBLoyalty)
                }
            }
        }
    }
}