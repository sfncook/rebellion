package com.rebllelionandroid.core.gameUpdater.events

import com.rebllelionandroid.core.database.gamestate.Planet
import com.rebllelionandroid.core.database.gamestate.Ship

data class ShipDestroyedEvent(val ship: Ship, val planet: Planet): UpdateEvent {
    override fun getEventMessage() = "Ship destroyed: ${ship.shipType} orbitting planet ${planet.name}."
}