package com.rebllelionandroid.core.gameUpdater.events

import com.rebllelionandroid.core.database.gamestate.Planet

data class PlanetConflictContinuesEvent(val planet: Planet): UpdateEvent {
    override fun getEventMessage() = "Planet is in conflict: ${planet.name}."
}