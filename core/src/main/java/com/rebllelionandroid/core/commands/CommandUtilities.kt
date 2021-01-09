package com.rebllelionandroid.core.commands

import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.enums.FactoryBuildTargetType
import com.rebllelionandroid.core.database.gamestate.enums.MissionTargetType
import com.rebllelionandroid.core.database.gamestate.enums.MissionType
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty

class CommandUtilities {
    companion object {
        private const val LOYALTY_MOD = 2

        fun moveUnitToPlanetSurface(
            gameStateViewModel: GameStateViewModel,
            unitId: Long,
            destPlanetId: Long,
            currentGameStateId: Long
        ) {
            gameStateViewModel.getPlanet(destPlanetId) { planet ->
                gameStateViewModel.getPersonnel(unitId) { unit ->
                    var newTeamALoyalty = planet.teamALoyalty
                    var newTeamBLoyalty = planet.teamBLoyalty
                    if(unit.team == TeamLoyalty.TeamA) {
                        newTeamALoyalty -= LOYALTY_MOD
                        newTeamBLoyalty += LOYALTY_MOD
                    } else {
                        newTeamALoyalty += LOYALTY_MOD
                        newTeamBLoyalty -= LOYALTY_MOD
                    }
                    gameStateViewModel.moveUnitToPlanet(
                        unitId,
                        destPlanetId,
                        currentGameStateId,
                        newTeamALoyalty.coerceAtLeast(0).coerceAtMost(100),
                        newTeamBLoyalty.coerceAtLeast(0).coerceAtMost(100)
                    )
                }
            }
        }

        fun moveUnitToShip(
            gameStateViewModel: GameStateViewModel,
            unitId: Long,
            destShipId: Long,
            currentGameStateId: Long
        ) {
            gameStateViewModel.getPersonnel(unitId) { unit ->
                // If unit is leaving a planet then update the planet's loyalties
                val srcPlanetId = unit.locationPlanetId
                if(srcPlanetId!=null) {
                    gameStateViewModel.getPlanet(srcPlanetId) { planet ->
                        var newTeamALoyalty = planet.teamALoyalty
                        var newTeamBLoyalty = planet.teamBLoyalty
                        if(unit.team == TeamLoyalty.TeamA) {
                            newTeamALoyalty += LOYALTY_MOD
                            newTeamBLoyalty -= LOYALTY_MOD
                        } else {
                            newTeamALoyalty -= LOYALTY_MOD
                            newTeamBLoyalty += LOYALTY_MOD
                        }
                        gameStateViewModel.moveUnitToShip(
                            unitId = unitId,
                            shipId = destShipId,
                            gameStateId = currentGameStateId,
                            srcPlanetId = srcPlanetId,
                            newTeamALoyalty = newTeamALoyalty.coerceAtLeast(0).coerceAtMost(100),
                            newTeamBLoyalty = newTeamBLoyalty.coerceAtLeast(0).coerceAtMost(100)
                        )
                    }
                } else {
                    gameStateViewModel.moveUnitToShip(
                        unitId = unitId,
                        shipId = destShipId,
                        gameStateId = currentGameStateId
                    )
                }
            }
        }

        fun moveShipToPlanet(
            gameStateViewModel: GameStateViewModel,
            shipId: Long,
            destPlanetId: Long,
            currentGameStateId: Long
        ) {
            gameStateViewModel.getShipWithUnits(shipId) { shipWithUnits ->
                if(shipWithUnits.ship.locationPlanetId != destPlanetId) {
                    gameStateViewModel.startShipJourneyToPlanet(shipId, destPlanetId, currentGameStateId)
                }
            }
        }

        fun factoryBuild(
            gameStateViewModel: GameStateViewModel,
            factoryId: Long,
            destPlanetId: Long,
            buildTargetType: FactoryBuildTargetType,
            currentGameStateId: Long
        ) {
            gameStateViewModel.getPlanetWithUnits(destPlanetId) { destPlanetWithUnits ->
                val factory = gameStateViewModel.getFactory(factoryId)
                val planetLoyalty = Utilities.getPlanetLoyalty(destPlanetWithUnits.planet)
                if(factory.team == planetLoyalty) {
                    if(
                        (Utilities.isBuildOrderForStructure(buildTargetType) && Utilities.getPlanetEnergiesEmpty(destPlanetWithUnits)>0) ||
                        !Utilities.isBuildOrderForStructure(buildTargetType)
                    ) {
                        gameStateViewModel.setFactoryBuildOrder(
                            factoryId,
                            destPlanetId,
                            buildTargetType,
                            currentGameStateId
                        )
                    } else {
                        println("ERROR: Build order for dest planet that does not have available energy slots")
                    }
                } else {
                    println("ERROR: Build order for factory and dest planet that aren't on the same team")
                }
            }
        }

        fun assignMission(
            gameStateViewModel: GameStateViewModel,
            personnelId: Long,
            currentGameStateId: Long,
            missionType: MissionType,
            missionTargetType: MissionTargetType,
            missionTargetId: Long
        ) {
            // Move unit to planet if currently on a ship
            gameStateViewModel.getPersonnel(personnelId) { personnel ->
                if(personnel.locationPlanetId==null) {
                    gameStateViewModel.getShip(personnel.locationShip!!) { ship ->
                        CommandUtilities.moveUnitToPlanetSurface(
                            gameStateViewModel,
                            personnelId,
                            ship.locationPlanetId,
                            currentGameStateId
                        )
                    }
                }
            }
            gameStateViewModel.assignMission(
                currentGameStateId,
                personnelId,
                missionType,
                missionTargetType,
                missionTargetId
            )
        }
    }
}
