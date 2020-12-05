package com.rebellionandroid.features.newgameactivity

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewGameActivity : AppCompatActivity() {
    @Inject lateinit var gameStateViewModel: GameStateViewModel
    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        initAppDependencyInjection()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game)
        val newGameBtn = findViewById<Button>(R.id.newgame_start_btn)
        newGameBtn.setOnClickListener {
            mainScope.launch(Dispatchers.IO) {
//                val gameStateId = gameStateViewModel.createNewGameState()
//                val gameStateSharedPrefFile = getString(R.string.gameStateSharedPrefFile)
//                val keyCurrentGameId = getString(R.string.keyCurrentGameId)
//                val sharedPref = getSharedPreferences(gameStateSharedPrefFile, Context.MODE_PRIVATE)
//                with (sharedPref.edit()) {
//                    putLong(keyCurrentGameId, gameStateId)
//                    commit()
//                }
//                finish()
            }
        }
    }

    private fun initAppDependencyInjection() {
        val gameStateComponent = DaggerGameStateComponent
            .builder()
            .contextModule(ContextModule(applicationContext))
            .build()
        gameStateViewModel = gameStateComponent.gameStateViewModel()
    }
}
