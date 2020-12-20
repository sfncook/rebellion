package com.rebllelionandroid.core.gameUpdater.events

import com.rebllelionandroid.core.database.gamestate.Planet
import com.rebllelionandroid.core.database.gamestate.Ship

data class ShipArrivalEvent(val ship: Ship, val planet: Planet): UpdateEvent {
    override fun getEventMessage() = "Ship (${ship.shipType.name}) arrived at planet ${planet.name}."
}