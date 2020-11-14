package com.rebllelionandroid.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebllelionandroid.core.database.gamestate.GameStateRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameStateViewModel @Inject constructor(val gameStateRepository: GameStateRepository) : ViewModel() {

    var i = 1
    lateinit var timerJob: Job

    fun startTimer() {
        timerJob = viewModelScope.launch {
            while (true) {
                println("my thread i:" + (i++))
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