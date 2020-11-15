package com.rebellionandroid.features.newgameactivity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.rebellionandroid.features.gamestateupdater.GameStateUpdater
import javax.inject.Inject

class NewGameActivity : Activity() {
    @Inject
    lateinit var gameStateUpdater: GameStateUpdater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game)

        val startBtn = findViewById<Button>(R.id.newgame_start_btn)
        startBtn.setOnClickListener {
            println("click")
        }
    }
}