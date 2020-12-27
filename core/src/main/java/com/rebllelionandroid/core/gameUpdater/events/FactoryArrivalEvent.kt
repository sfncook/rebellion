package com.rebllelionandroid.core.gameUpdater.events

import com.rebllelionandroid.core.database.gamestate.Factory
import com.rebllelionandroid.core.database.gamestate.Planet

data class FactoryArrivalEvent(val factory: Factory, val planet: Planet): UpdateEvent {
    override fun getEventMessage() = "Factory (${factory.factoryType.name}) arrived at planet ${planet.name}."
}