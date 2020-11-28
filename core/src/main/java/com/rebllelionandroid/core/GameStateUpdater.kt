package com.rebllelionandroid.core

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import javax.inject.Inject

class GameStateUpdater @Inject constructor(
        gameStateViewModel: GameStateViewModel,
        context: Context
) {
    init {
        println("Init block")
        if(context is AppCompatActivity) {
            gameStateViewModel.gameStateLive.observe(context, {
                println("Updater gameStateLive notification")
            })
        } else {
            println("[ERROR] GameStateUpdater.init context is not of type AppCompatActivity!  Unable to observe gameState view model")
        }
    }
}
