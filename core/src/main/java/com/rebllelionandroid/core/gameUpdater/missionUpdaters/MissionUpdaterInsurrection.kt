package com.rebllelionandroid.core.gameUpdater.missionUpdaters

import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.Personnel
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.gamestate.enums.MissionType
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import com.rebllelionandroid.core.gameUpdater.events.MissionFailureEvent
import com.rebllelionandroid.core.gameUpdater.events.MissionSuccessEvent
import com.rebllelionandroid.core.gameUpdater.events.UpdateEvent

class MissionUpdaterInsurrection: MissionUpdater() {

    override fun handleMission(personnel: Personnel): Boolean {
        return personnel.missionType == MissionType.Insurrection
    }

    override fun getSuccessThreshold(planetWithUnits: PlanetWithUnits, personnel: Personnel): Int {
        val planetEnemyTeamLoyalty: Int = if(personnel.team== TeamLoyalty.TeamA)
            planetWithUnits.planet.teamBLoyalty
        else
            planetWithUnits.planet.teamALoyalty
        return when(planetEnemyTeamLoyalty) {
            in 0..24 -> 90
            in 25..49 -> 70
            in 50..74 -> 35
            else -> 20
        }
    }

    override fun onMissionSuccess(
        updateEvents: MutableList<UpdateEvent>,
        planetWithUnits: PlanetWithUnits,
        personnel: Personnel
    ) {
        // Successful Insurrection mission reduces enemy's standing/loyalty with planet
        if(personnel.team== TeamLoyalty.TeamA) {
            planetWithUnits.planet.teamBLoyalty -= 25
        } else {

            planetWithUnits.planet.teamALoyalty -= 25
        }
        planetWithUnits.planet.teamALoyalty = planetWithUnits.planet.teamALoyalty.coerceAtLeast(0).coerceAtMost(100)
        planetWithUnits.planet.teamBLoyalty = planetWithUnits.planet.teamBLoyalty.coerceAtLeast(0).coerceAtMost(100)
        planetWithUnits.planet.uprisingRank = Utilities.increaseUprisingRank(planetWithUnits.planet.uprisingRank)
        planetWithUnits.planet.updated = true
        updateEvents.add(MissionSuccessEvent("SpecOps unit successfully provoked insurrection on Planet ${planetWithUnits.planet.name}"))
    }

    override fun onMissionFailure(
        updateEvents: MutableList<UpdateEvent>,
        planetWithUnits: PlanetWithUnits,
        personnel: Personnel
    ) {
        updateEvents.add(MissionFailureEvent("SpecOps unit failed to provoke insurrection on Planet ${planetWithUnits.planet.name}"))
    }
}
