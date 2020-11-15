package com.rebllelionandroid.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebllelionandroid.core.database.gamestate.GameStateRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameTimerViewModel @Inject constructor(val gameStateRepository: GameStateRepository): ViewModel() {
    lateinit var timerJob: Job
    val time: MutableLiveData<Int> = MutableLiveData(0)
    val gameState = gameStateRepository.getGameState()

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
}
