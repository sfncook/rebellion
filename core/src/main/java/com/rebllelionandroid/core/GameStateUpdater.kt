package com.rebllelionandroid.core

import com.rebllelionandroid.core.database.gamestate.GameState
import javax.inject.Inject
import kotlin.random.Random

class GameStateUpdater @Inject constructor(
    private val gameStateViewModel: GameStateViewModel
) {
//    fun createNewGame() {
//        val gameState = GameState(Random.nextLong(), false, 1)
//        gameStateViewModel.createNewGameState(gameState)
//    }
}
