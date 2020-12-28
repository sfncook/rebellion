package com.rebllelionandroid.core.gameUpdater.events

import com.rebllelionandroid.core.database.gamestate.Planet
import com.rebllelionandroid.core.database.gamestate.Ship

data class ShipBuiltEvent(val ship: Ship, val planet: Planet): UpdateEvent {
    override fun getEventMessage(): String {
        if(ship.isTraveling) {
            return "Ship (${ship.shipType.name}) finished building and will arrive on planet ${planet.name} day:${ship.dayArrival}."
        } else {
            return "Ship (${ship.shipType.name}) finished building on planet ${planet.name}."
        }
    }
}