package com.rebllelionandroid.core

import com.rebllelionandroid.core.database.gamestate.GameState
import com.rebllelionandroid.core.database.gamestate.GameStateRepository
import javax.inject.Inject

class GameStateUpdater @Inject constructor(
    val gameStateRepository: GameStateRepository
) {
    fun createNewGame() {
        gameStateRepository.createNewGameState(GameState())
        val allGameStates = gameStateRepository.getAllGameStates()
        println("")
    }

    fun onUpdate() {

    }
}
