package com.rebellionandroid.features.gamestateupdater

import com.rebllelionandroid.core.database.gamestate.GameStateRepository
import javax.inject.Inject

class GameStateUpdater @Inject constructor(
    val gameStateRepository: GameStateRepository
) {
    fun onUpdate() {

    }
}
