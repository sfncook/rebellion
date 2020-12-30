package com.rebllelionandroid.core.gameUpdater.events

import com.rebllelionandroid.core.database.gamestate.Planet
import com.rebllelionandroid.core.database.gamestate.Personnel

data class NewUnitTrainedEvent(val personnel: Personnel, val planet: Planet): UpdateEvent {
    override fun getEventMessage(): String {
        if(personnel.isTraveling) {
            return "Ship (${personnel.unitType.name}) finished building and will arrive on planet ${planet.name} day:${personnel.dayArrival}."
        } else {
            return "Ship (${personnel.unitType.name}) finished building on planet ${planet.name}."
        }
    }
}