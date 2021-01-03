package com.rebllelionandroid.core.gameUpdater.missionUpdaters

import com.rebllelionandroid.core.database.gamestate.GameStateWithSectors
import com.rebllelionandroid.core.database.gamestate.Personnel
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.gameUpdater.events.UpdateEvent
import kotlin.random.Random

abstract class MissionUpdater {
    protected val NO_OP_SUCCESS_THRESHOLD: Int = 999

    protected abstract fun handleMission(personnel: Personnel): Boolean
    protected abstract fun getSuccessThreshold(planetWithUnits: PlanetWithUnits, personnel: Personnel): Int
    protected abstract fun onMissionSuccess(
        updateEvents: MutableList<UpdateEvent>,
        planetWithUnits: PlanetWithUnits,
        personnel: Personnel
    )
    protected abstract fun onMissionFailure(
        updateEvents: MutableList<UpdateEvent>,
        planetWithUnits: PlanetWithUnits,
        personnel: Personnel
    )

    fun update(
        gameStateWithSectors: GameStateWithSectors,
        updateEvents: MutableList<UpdateEvent>,
        planetWithUnits: PlanetWithUnits,
        personnel: Personnel
    ) {
        val gameTime = gameStateWithSectors.gameState.gameTime
        if(
            personnel.missionType!=null &&
            handleMission(personnel) &&
            personnel.dayMissionComplete!! <= gameTime
        ) {
            val successThreshold =  getSuccessThreshold(planetWithUnits, personnel)
            val success = Random.nextInt(0, 100) <= successThreshold
            if(success) {
                onMissionSuccess(updateEvents, planetWithUnits, personnel)
            } else {
                onMissionFailure(updateEvents, planetWithUnits, personnel)
            }
            personnel.missionTargetType = null
            personnel.missionType = null
            personnel.missionTargetId = null
            personnel.dayMissionComplete = null
            personnel.updated = true
        }
    }
}