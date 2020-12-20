package com.rebllelionandroid.core.gameUpdater.events

import com.rebllelionandroid.core.database.gamestate.Planet

data class PlanetConflictStartsEvent(val planet: Planet): UpdateEvent {
    override fun getEventMessage() = "Planet is in conflict: ${planet.name}."
}