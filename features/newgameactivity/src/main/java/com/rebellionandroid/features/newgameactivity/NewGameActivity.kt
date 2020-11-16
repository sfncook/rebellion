package com.rebellionandroid.features.newgameactivity

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import com.rebllelionandroid.core.GameStateUpdater
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule
import javax.inject.Inject

class NewGameActivity : Activity() {
    @Inject
    lateinit var gameStateUpdater: GameStateUpdater

    override fun onCreate(savedInstanceState: Bundle?) {
        initAppDependencyInjection()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game)

        val startBtn = findViewById<Button>(R.id.newgame_start_btn)
        startBtn.setOnClickListener {
            gameStateUpdater.createNewGame()
        }
    }

    private fun initAppDependencyInjection() {
        val gameStateComponent = DaggerGameStateComponent
            .builder()
            .contextModule(ContextModule(applicationContext))
            .build()
        gameStateUpdater = gameStateComponent.gameStateUpdater()
    }
}
