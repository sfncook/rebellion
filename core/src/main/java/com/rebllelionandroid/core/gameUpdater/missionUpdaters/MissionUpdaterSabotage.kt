package com.rebllelionandroid.core.gameUpdater.missionUpdaters

import com.rebllelionandroid.core.database.gamestate.Personnel
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.gamestate.enums.MissionTargetType
import com.rebllelionandroid.core.database.gamestate.enums.MissionType
import com.rebllelionandroid.core.database.gamestate.enums.ShipType
import com.rebllelionandroid.core.database.gamestate.enums.UnitType
import com.rebllelionandroid.core.gameUpdater.events.MissionFailureEvent
import com.rebllelionandroid.core.gameUpdater.events.MissionSuccessEvent
import com.rebllelionandroid.core.gameUpdater.events.UpdateEvent

class MissionUpdaterSabotage: MissionUpdater() {

    override fun handleMission(personnel: Personnel): Boolean {
        return personnel.missionType == MissionType.Sabotage
    }

    override fun getSuccessThreshold(planetWithUnits: PlanetWithUnits, personnel: Personnel): Int {
        return when(personnel.missionTargetType) {
            MissionTargetType.Unit -> {
                val targetPersonnel = planetWithUnits.personnels.find { it.id==personnel.missionTargetId!! }
                when(targetPersonnel?.unitType) {
                    UnitType.SpecialForces -> 50
                    UnitType.Garrison -> 30
                    else -> NO_OP_SUCCESS_THRESHOLD
                }
            }
            MissionTargetType.Factory -> 20
            MissionTargetType.Ship -> {
                val targetShip = planetWithUnits.shipsWithUnits.find { it.ship.id==personnel.missionTargetId!! }
                when(targetShip?.ship?.shipType) {
                    ShipType.Bireme -> 60
                    ShipType.Trireme -> 50
                    ShipType.Quadrireme -> 40
                    ShipType.Quinquereme -> 35
                    ShipType.Hexareme -> 30
                    ShipType.Septireme -> 25
                    ShipType.Octere -> 20
                    else -> NO_OP_SUCCESS_THRESHOLD
                }
            }
            MissionTargetType.DefenseStructure -> 30
            else -> NO_OP_SUCCESS_THRESHOLD
        }
    }

    override fun onMissionSuccess(
        updateEvents: MutableList<UpdateEvent>,
        planetWithUnits: PlanetWithUnits,
        personnel: Personnel
    ) {
        var targetTypeName: String = ""
        when(personnel.missionTargetType) {
            MissionTargetType.Unit -> {
                val target = planetWithUnits.personnels.find { it.id==personnel.missionTargetId!! }
                if(target!=null) {
                    target.destroyed = true
                    targetTypeName = target.unitType.value
                }
            }
            MissionTargetType.Factory -> {
                val target = planetWithUnits.factories.find { it.id==personnel.missionTargetId!! }
                if(target!=null) {
                    target.destroyed = true
                    targetTypeName = target.factoryType.value
                }
            }
            MissionTargetType.Ship -> {
                val target = planetWithUnits.shipsWithUnits.find { it.ship.id==personnel.missionTargetId!! }
                if(target!=null) {
                    target.ship.destroyed = true
                    targetTypeName = target.ship.shipType.value
                }
            }
            MissionTargetType.DefenseStructure -> {
                val target = planetWithUnits.defenseStructures.find { it.id==personnel.missionTargetId!! }
                if(target!=null) {
                    target.destroyed = true
                    targetTypeName = target.defenseStructureType.value
                }
            }
            else -> {}
        }
        updateEvents.add(MissionSuccessEvent("SpecOps unit successfully destroyed target $targetTypeName"))
    }

    override fun onMissionFailure(
        updateEvents: MutableList<UpdateEvent>,
        planetWithUnits: PlanetWithUnits,
        personnel: Personnel
    ) {
        var targetTypeName: String = ""
        when(personnel.missionTargetType) {
            MissionTargetType.Unit -> {
                val target = planetWithUnits.personnels.find { it.id==personnel.missionTargetId!! }
                if(target!=null) {
                    targetTypeName = target.unitType.value
                }
            }
            MissionTargetType.Factory -> {
                val target = planetWithUnits.factories.find { it.id==personnel.missionTargetId!! }
                if(target!=null) {
                    targetTypeName = target.factoryType.value
                }
            }
            MissionTargetType.Ship -> {
                val target = planetWithUnits.shipsWithUnits.find { it.ship.id==personnel.missionTargetId!! }
                if(target!=null) {
                    targetTypeName = target.ship.shipType.value
                }
            }
            MissionTargetType.DefenseStructure -> {
                val target = planetWithUnits.defenseStructures.find { it.id==personnel.missionTargetId!! }
                if(target!=null) {
                    targetTypeName = target.defenseStructureType.value
                }
            }
            else -> {}
        }
        updateEvents.add(MissionFailureEvent("SpecOps unit failed to destroy target $targetTypeName"))
    }
}