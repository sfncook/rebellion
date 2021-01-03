package com.rebellionandroid.components.commands

import android.os.Bundle
import com.rebellionandroid.components.commands.enums.OrderDlgArgumentKeys
import com.rebellionandroid.components.commands.enums.OrderProcedures
import com.rebellionandroid.components.commands.orderComponents.OrderComponent
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.enums.FactoryBuildTargetType
import com.rebllelionandroid.core.database.gamestate.enums.MissionTargetType
import com.rebllelionandroid.core.database.gamestate.enums.MissionType
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty

class CommandUtilities {
    companion object {
        private const val LOYALTY_MOD = 2
        private const val PLANET_LOYALTY_THRESHOLD = 50

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

        private fun moveShipToPlanet(
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

        private fun factoryBuild(
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

        fun orderComponentsToMap(orderComponents: List<OrderComponent>): Map<String, String?> {
            val orderParameters = mutableMapOf<String, String?>()
            orderComponents.forEach { orderComponent ->
                orderParameters.putAll(orderComponent.getSelectedValue())
            }
            return orderParameters
        }

        fun conductOrderProcedures(
            gameStateViewModel: GameStateViewModel,
            bundle: Bundle,
            orderParameters: Map<String, String?>,
            currentGameStateId: Long
        ) {
            when(bundle.get(OrderDlgArgumentKeys.OrderProcedure.value) as OrderProcedures) {
                OrderProcedures.MoveShip -> {
                    val shipId = bundle.getLong(OrderDlgArgumentKeys.MoveShipId.value)
                    val destPlanetId = orderParameters[OrderDlgArgumentKeys.SelectedPlanetId.value]
                    if (destPlanetId != null) {
                        moveShipToPlanet(gameStateViewModel, shipId, destPlanetId.toLong(), currentGameStateId)
                    } else {
                        println("ERROR: MoveShip missing parameters")
                    }
                }

                OrderProcedures.FactoryBuild -> {
                    val factoryId = bundle.getLong(OrderDlgArgumentKeys.FactoryId.value)
                    val destPlanetId = orderParameters[OrderDlgArgumentKeys.SelectedPlanetId.value]
                    val buildTargetTypeStr = orderParameters[OrderDlgArgumentKeys.BuildTargetType.value]
                    if(buildTargetTypeStr != null) {
                        val buildTargetType = FactoryBuildTargetType.valueOf(buildTargetTypeStr)
                        if (destPlanetId != null) {
                            factoryBuild(
                                gameStateViewModel,
                                factoryId,
                                destPlanetId.toLong(),
                                buildTargetType,
                                currentGameStateId
                            )
                        } else {
                            println("ERROR: MoveShip missing parameters")
                        }
                    }
                }

                OrderProcedures.AssignMission -> {
                    //TODO: Move unit to planet if on ship
                    val personnelId = bundle.getLong(OrderDlgArgumentKeys.PersonnelId.value)
                    val missionTypeStr = orderParameters[OrderDlgArgumentKeys.MissionType.value]
                    val missionTargetTypeStr = orderParameters[OrderDlgArgumentKeys.MissionTargetType.value]
                    val missionTargetId = orderParameters[OrderDlgArgumentKeys.MissionTargetId.value]
                    if(missionTypeStr!=null && missionTargetTypeStr!=null && missionTargetId!=null) {
                        val missionType = MissionType.valueOf(missionTypeStr)
                        val missionTargetType = MissionTargetType.valueOf(missionTargetTypeStr)
                        gameStateViewModel.assignMission(
                            currentGameStateId,
                            personnelId,
                            missionType,
                            missionTargetType,
                            missionTargetId.toLong()
                        )
                    } else {
                        println("ERROR: assign mission missing parameters")
                    }
                }

                else -> {}
            }// when orderProcedure
        }
    }
}
