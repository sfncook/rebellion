package com.rebllelionandroid.core.gameUpdater.missionUpdaters

import com.rebllelionandroid.core.database.gamestate.Personnel
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.gamestate.enums.MissionTargetType
import com.rebllelionandroid.core.database.gamestate.enums.MissionType
import com.rebllelionandroid.core.database.gamestate.enums.ShipType
import com.rebllelionandroid.core.database.gamestate.enums.UnitType
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import com.rebllelionandroid.core.gameUpdater.events.MissionFailureEvent
import com.rebllelionandroid.core.gameUpdater.events.MissionSuccessEvent
import com.rebllelionandroid.core.gameUpdater.events.UpdateEvent

class MissionUpdaterIntelligence: MissionUpdater() {

    override fun handleMission(personnel: Personnel): Boolean {
        return personnel.missionType == MissionType.Intelligence
    }

    override fun getSuccessThreshold(planetWithUnits: PlanetWithUnits, personnel: Personnel): Int {
        // TODO
        return 100
    }

    override fun onMissionSuccess(
        updateEvents: MutableList<UpdateEvent>,
        planetWithUnits: PlanetWithUnits,
        personnel: Personnel
    ) {
        // TODO
    }

    override fun onMissionFailure(
        updateEvents: MutableList<UpdateEvent>,
        planetWithUnits: PlanetWithUnits,
        personnel: Personnel
    ) {
        // TODO
    }
}
