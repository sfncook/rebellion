package com.rebllelionandroid.core.gameUpdater.events

import com.rebllelionandroid.core.database.gamestate.Planet
import com.rebllelionandroid.core.database.gamestate.Unit

data class NewUnitTrainedEvent(val unit: Unit, val planet: Planet): UpdateEvent {
    override fun getEventMessage(): String {
//        if(unit.isTraveling) {
//            return "Ship (${ship.shipType.name}) finished building and will arrive on planet ${planet.name} day:${ship.dayArrival}."
//        } else {
//            return "Ship (${ship.shipType.name}) finished building on planet ${planet.name}."
//        }
        return ""
    }
}