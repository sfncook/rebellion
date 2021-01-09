package com.rebllelionandroid.core.gameUpdater

import com.rebllelionandroid.core.database.gamestate.GameStateWithSectors

interface Updater {
    fun updateGameState(gameStateWithSectors: GameStateWithSectors): GameUpdateResponse
}