package com.rebllelionandroid.core.gameUpdater.missionUpdaters

import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.database.gamestate.GameStateWithSectors
import com.rebllelionandroid.core.database.gamestate.Personnel
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.gameUpdater.events.UpdateEvent

abstract class MissionUpdater {
    protected val NO_OP_SUCCESS_THRESHOLD: Int = 999
    abstract fun update(
        gameStateWithSectors: GameStateWithSectors,
        updateEvents: MutableList<UpdateEvent>,
        planetWithUnits: PlanetWithUnits,
        personnel: Personnel
    )
}