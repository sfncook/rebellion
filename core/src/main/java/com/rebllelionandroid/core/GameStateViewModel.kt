package com.rebllelionandroid.core

import androidx.lifecycle.ViewModel
import com.rebllelionandroid.core.database.gamestate.GameStateRepository
import javax.inject.Inject

class GameStateViewModel @Inject constructor(val gameStateRepository: GameStateRepository) : ViewModel() {

}