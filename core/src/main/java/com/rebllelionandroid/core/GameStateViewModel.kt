package com.rebllelionandroid.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebllelionandroid.core.database.gamestate.GameState
import com.rebllelionandroid.core.database.gamestate.GameStateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class GameStateViewModel @Inject constructor(val gameStateRepository: GameStateRepository) : ViewModel() {
    lateinit var timerJob: Job
    val time: MutableLiveData<Int> = MutableLiveData(0)
    val gameStateLive = gameStateRepository.getGameStateLive()

    fun startTimer() {
        timerJob = viewModelScope.launch {
            while (true) {
                val timeVal = time.value?.plus(1)
                time.value = timeVal
                println("my thread i:$timeVal")
                delay(2000)
            }
        }
    }

    fun stopTimer() {
        if(this::timerJob.isInitialized) {
            timerJob.cancel()
        }
    }

    fun createNewGameState() {
        val gameState = GameState(Random.nextLong(), false, 1)
        viewModelScope.launch(Dispatchers.IO) {
            gameStateRepository.createNewGameState(gameState)
            println("")
        }
    }

    fun getGameState(): GameState {
        return gameStateRepository.getGameState()
    }
}