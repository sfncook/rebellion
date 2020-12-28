package com.rebllelionandroid.core.gameUpdater.events

import com.rebllelionandroid.core.database.gamestate.Factory
import com.rebllelionandroid.core.database.gamestate.Planet

data class FactoryBuiltEvent(val factory: Factory, val planet: Planet): UpdateEvent {
    override fun getEventMessage(): String {
        if(factory.isTravelling) {
            return "Factory (${factory.factoryType.name}) finished building and will arrive on planet ${planet.name} day:${factory.dayArrival}."
        } else {
            return "Factory (${factory.factoryType.name}) finished building on planet ${planet.name}."
        }
    }
}