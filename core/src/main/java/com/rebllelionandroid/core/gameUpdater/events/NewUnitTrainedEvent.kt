package com.rebllelionandroid.core.gameUpdater.events

import com.rebllelionandroid.core.database.gamestate.Planet
import com.rebllelionandroid.core.database.gamestate.Unit

data class NewUnitTrainedEvent(val unit: Unit, val planet: Planet): UpdateEvent {
    override fun getEventMessage(): String {
        if(unit.isTraveling) {
            return "Ship (${unit.unitType.name}) finished building and will arrive on planet ${planet.name} day:${unit.dayArrival}."
        } else {
            return "Ship (${unit.unitType.name}) finished building on planet ${planet.name}."
        }
    }
}