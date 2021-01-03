package com.rebllelionandroid.core.gameUpdater.missionUpdaters

import com.rebllelionandroid.core.database.gamestate.Personnel
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.gamestate.enums.MissionType
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import com.rebllelionandroid.core.gameUpdater.events.MissionFailureEvent
import com.rebllelionandroid.core.gameUpdater.events.MissionSuccessEvent
import com.rebllelionandroid.core.gameUpdater.events.UpdateEvent

class MissionUpdaterDiplomacy: MissionUpdater() {

    override fun handleMission(personnel: Personnel): Boolean {
        return personnel.missionType == MissionType.Diplomacy
    }

    override fun getSuccessThreshold(planetWithUnits: PlanetWithUnits, personnel: Personnel): Int {
        val planetTeamLoyalty: Int = if(personnel.team==TeamLoyalty.TeamA)
            planetWithUnits.planet.teamALoyalty
        else
            planetWithUnits.planet.teamBLoyalty
        return when(planetTeamLoyalty) {
            in 0..24 -> 25
            in 25..49 -> 35
            in 50..74 -> 45
            else -> 60
        }
    }

    override fun onMissionSuccess(
        updateEvents: MutableList<UpdateEvent>,
        planetWithUnits: PlanetWithUnits,
        personnel: Personnel
    ) {
        if(personnel.team==TeamLoyalty.TeamA) {
            planetWithUnits.planet.teamALoyalty += 10
            planetWithUnits.planet.teamBLoyalty -= 10
        } else {

            planetWithUnits.planet.teamALoyalty -= 10
            planetWithUnits.planet.teamBLoyalty += 10
        }
        planetWithUnits.planet.teamALoyalty = planetWithUnits.planet.teamALoyalty.coerceAtLeast(0).coerceAtMost(100)
        planetWithUnits.planet.teamBLoyalty = planetWithUnits.planet.teamBLoyalty.coerceAtLeast(0).coerceAtMost(100)
        planetWithUnits.planet.updated = true
        updateEvents.add(MissionSuccessEvent("SpecOps unit successfully improved relations with Planet ${planetWithUnits.planet.name}"))
    }

    override fun onMissionFailure(
        updateEvents: MutableList<UpdateEvent>,
        planetWithUnits: PlanetWithUnits,
        personnel: Personnel
    ) {
        updateEvents.add(MissionFailureEvent("SpecOps unit failed to improve relations with Planet ${planetWithUnits.planet.name}"))
    }
}
