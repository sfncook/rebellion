package com.rebellionandroid.components.gamecontrols

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel

class GameControlsFragment: Fragment() {

    private lateinit var gameStateViewModel: GameStateViewModel

    private lateinit var playPauseBtn: MaterialButton
    private lateinit var gameTimeText: TextView
    private var currentGameStateId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        gameStateViewModel = (activity as BaseActivity).gameStateViewModel

        val root = inflater.inflate(R.layout.fragment_game_controls, container, false)

        gameTimeText = root.findViewById(R.id.ctl_time)
        playPauseBtn = root.findViewById(R.id.ctl_play_pause)
        playPauseBtn.setOnClickListener {
            val gameState = gameStateViewModel.getGameState(currentGameStateId)
            if(gameState.gameInProgress) {
                gameStateViewModel.stopTimer(currentGameStateId)
            } else {
                gameStateViewModel.startTimer(currentGameStateId)
            }
            updatePlayButton(!gameState.gameInProgress)
        }

        return root
    }

    override fun onResume() {
        super.onResume()

        val gameStateSharedPrefFile = getString(R.string.gameStateSharedPrefFile)
        val keyCurrentGameId = getString(R.string.keyCurrentGameId)
        val sharedPref = activity?.getSharedPreferences(gameStateSharedPrefFile, Context.MODE_PRIVATE)
        if (sharedPref != null) {
            if(sharedPref.contains(keyCurrentGameId)) {
                currentGameStateId = sharedPref.getLong(keyCurrentGameId, 0)
            } else {
                println("ERROR No current game ID foudn in shared preferences")
            }
        }

        gameStateViewModel.getGameStateWithSectorsLive(currentGameStateId).observe(viewLifecycleOwner, {
            gameTimeText.text = it.gameState.gameTime.toString()
            updatePlayButton(it.gameState.gameInProgress)
        })
    }

    override fun onStop() {
        super.onStop()
        gameStateViewModel.stopTimer(currentGameStateId)
    }

    private fun updatePlayButton(gameInProgress: Boolean) {
        if(gameInProgress) {
            playPauseBtn.setIconResource(R.drawable.ic_baseline_pause_circle_outline_24)
        } else {
            playPauseBtn.setIconResource(R.drawable.ic_baseline_play_circle_outline_24)
        }
    }
}
