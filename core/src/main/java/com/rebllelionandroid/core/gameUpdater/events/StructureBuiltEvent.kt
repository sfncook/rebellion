package com.rebllelionandroid.core.gameUpdater.events

import com.rebllelionandroid.core.database.gamestate.DefenseStructure
import com.rebllelionandroid.core.database.gamestate.Planet

data class StructureBuiltEvent(val structure: DefenseStructure, val planet: Planet): UpdateEvent {
    override fun getEventMessage(): String {
        if(structure.isTraveling) {
            return "Structure (${structure.defenseStructureType.value}) finished building and will arrive on planet ${planet.name} day:${structure.dayArrival}."
        } else {
            return "Structure (${structure.defenseStructureType.value}) finished building on planet ${planet.name}."
        }
    }
}