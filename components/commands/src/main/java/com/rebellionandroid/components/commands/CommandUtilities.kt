package com.rebellionandroid.components.commands

import android.os.Bundle
import com.rebellionandroid.components.commands.enums.OrderDlgArgumentKeys
import com.rebellionandroid.components.commands.enums.OrderProcedures
import com.rebellionandroid.components.commands.orderComponents.OrderComponent
import com.rebllelionandroid.core.GameStateViewModel
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
                gameStateViewModel.getUnit(unitId) { unit ->
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
            gameStateViewModel.getUnit(unitId) { unit ->
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

        fun conductOrderProcedures(
            gameStateViewModel: GameStateViewModel,
            bundle: Bundle,
            orderComponents: List<OrderComponent>,
            currentGameStateId: Long
        ) {
            val orderProcedure = bundle.get(OrderDlgArgumentKeys.OrderProcedure.value) as OrderProcedures
            val orderParameters = orderComponents.associateBy({it->it.getSelectedValue().first}, {it->it.getSelectedValue().second})
            when(orderProcedure) {
                OrderProcedures.MoveShip -> {
                    val shipId = bundle.getLong(OrderDlgArgumentKeys.MoveShipId.value)
                    val destPlanetId = orderParameters[OrderDlgArgumentKeys.SelectedPlanetId.value]
                    if (destPlanetId != null) {
                        moveShipToPlanet(gameStateViewModel, shipId, destPlanetId.toLong(), currentGameStateId)
                    } else {
                        println("ERROR: MoveShip missing parameters")
                    }
                }
            }
        }
    }
}
