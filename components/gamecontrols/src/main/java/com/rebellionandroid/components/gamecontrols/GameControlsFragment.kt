package com.rebellionandroid.components.gamecontrols

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.google.android.material.button.MaterialButton
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.database.gamestate.GameStateWithSectors

class GameControlsFragment: Fragment() {

    private lateinit var gameStateViewModel: GameStateViewModel
    private lateinit var gameStateLive: LiveData<GameStateWithSectors>
    private lateinit var playPauseBtn: MaterialButton
    private lateinit var gameTimeText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_game_controls, container, false)

        gameStateViewModel = (activity as BaseActivity).gameStateViewModel

        gameTimeText = root.findViewById(R.id.ctl_time)
        playPauseBtn = root.findViewById(R.id.ctl_play_pause)
        playPauseBtn.setOnClickListener {
            val currentGameStateId = getCurrentGameStateId()
            if(currentGameStateId!=null) {
                gameStateViewModel.toggleTimer(currentGameStateId)
            }
        }

        gameStateLive = gameStateViewModel.gameState
        gameStateLive.observe(viewLifecycleOwner , { gameStateWithSectors ->
            gameTimeText.text = gameStateWithSectors.gameState.gameTime.toString()
            updatePlayButton(gameStateWithSectors.gameState.gameInProgress)
        })

        return root
    }

    private fun updatePlayButton(gameInProgress: Boolean) {
        if(gameInProgress) {
            playPauseBtn.setIconResource(R.drawable.ic_baseline_pause_circle_outline_24)
        } else {
            playPauseBtn.setIconResource(R.drawable.ic_baseline_play_circle_outline_24)
        }
    }

    private fun getCurrentGameStateId(): Long?  {
        val gameStateSharedPrefFile = getString(R.string.gameStateSharedPrefFile)
        val keyCurrentGameId = getString(R.string.keyCurrentGameId)
        val sharedPref = activity?.getSharedPreferences(gameStateSharedPrefFile, Context.MODE_PRIVATE)
        return sharedPref?.getLong(keyCurrentGameId, 0)
    }
}
