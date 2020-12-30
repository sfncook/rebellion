package com.rebllelionandroid.core.gameUpdater

import com.rebllelionandroid.core.database.gamestate.Factory
import com.rebllelionandroid.core.database.gamestate.GameStateWithSectors
import com.rebllelionandroid.core.database.gamestate.Ship
import com.rebllelionandroid.core.database.gamestate.Unit
import com.rebllelionandroid.core.gameUpdater.events.UpdateEvent

data class GameUpdateResponse(
    val gameStateWithSectors: GameStateWithSectors,
    val updateEvents: List<UpdateEvent>,
    val newFactories: List<Factory>,
    val newShips: List<Ship>,
    val newUnits: List<Unit>
)
