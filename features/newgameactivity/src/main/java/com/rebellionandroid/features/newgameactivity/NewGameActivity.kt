package com.rebellionandroid.features.newgameactivity

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.rebellionandroid.features.newgameactivity.databinding.ActivityNewGameBinding
import com.rebllelionandroid.core.GameStateUpdater
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.launch

class NewGameActivity : AppCompatActivity() {
    @Inject lateinit var gameStateUpdater: GameStateUpdater
    @Inject lateinit var gameStateViewModel: GameStateViewModel
//    val uiScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        initAppDependencyInjection()
        super.onCreate(savedInstanceState)

        val binding: ActivityNewGameBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_game)
        binding.lifecycleOwner = this
        binding.viewModel = gameStateViewModel
//        setContentView(R.layout.activity_new_game)

//        val startBtn = findViewById<Button>(R.id.newgame_start_btn)
//        startBtn.setOnClickListener {
//            uiScope.launch {
//                gameStateUpdater.createNewGame()
//                finish()
//            }
//        }
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
