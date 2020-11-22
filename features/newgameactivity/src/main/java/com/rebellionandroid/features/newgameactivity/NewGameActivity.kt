package com.rebellionandroid.features.newgameactivity

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.rebllelionandroid.core.GameStateUpdater
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewGameActivity : AppCompatActivity() {
    @Inject lateinit var gameStateUpdater: GameStateUpdater
    @Inject lateinit var gameStateViewModel: GameStateViewModel
    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        initAppDependencyInjection()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game)
        val newGameBtn = findViewById<Button>(R.id.newgame_start_btn)
        newGameBtn.setOnClickListener {
            mainScope.launch(Dispatchers.IO) {
                gameStateViewModel.createNewGameState()
                finish()
            }
        }
    }

    private fun initAppDependencyInjection() {
        val gameStateComponent = DaggerGameStateComponent
            .builder()
            .contextModule(ContextModule(applicationContext))
            .build()
        gameStateUpdater = gameStateComponent.gameStateUpdater()
        gameStateViewModel = gameStateComponent.gameStateViewModel()
    }
}
