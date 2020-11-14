package com.rebllelionandroid.core.database.gamestate

import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.PRIVATE
import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rebllelionandroid.core.database.gamestate.GameState
import com.rebllelionandroid.core.database.gamestate.GameStateDao
import javax.inject.Inject

class GameStateRepository @Inject constructor(
    @VisibleForTesting(otherwise = PRIVATE)
    internal val gameStateDao: GameStateDao
) {


    fun getAllCharactersFavoriteLiveData(): LiveData<List<GameState>> =
            gameStateDao.getAllGameStates()

    fun createNewGameState(gameState: GameState) =
            gameStateDao.createNewGameState(gameState)

    fun updateGameState(gameState: GameState) =
            gameStateDao.updateGameState(gameState)
}
