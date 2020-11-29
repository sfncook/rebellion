package com.rebellionandroid.components.gamecontrols

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.rebellionandroid.components.gamecontrols.databinding.FragmentGameControlsBinding
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel
import javax.inject.Inject

class GameControlsFragment: Fragment() {

    @Inject
    lateinit var gameStateViewModel: GameStateViewModel

    lateinit var playPauseBtn: MaterialButton
    lateinit var gameTimeText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //https://developer.android.com/training/dependency-injection/dagger-multi-module
        val gameStateComponent = (activity as BaseActivity).gameStateComponent
        DaggerGameControlsComponent.factory().create(gameStateComponent).inject(this)

        val viewBinding: FragmentGameControlsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_controls, container, false)
        viewBinding.lifecycleOwner = viewLifecycleOwner

        gameTimeText = viewBinding.root.findViewById(R.id.ctl_time)
        playPauseBtn = viewBinding.root.findViewById<MaterialButton>(R.id.ctl_play_pause)
        playPauseBtn.setOnClickListener {
            if(gameStateViewModel.gameStateLive.value?.gameInProgress!!) {
                gameStateViewModel.stopTimer()
            } else {
                gameStateViewModel.startTimer()
            }
            updatePlayButton()
        }

        gameStateViewModel.gameStateLive.observe(viewLifecycleOwner, Observer {
            println("GameControlsFragment gameStateLive observe notification")
            gameTimeText.text = it.gameTime.toString()
        })

        gameStateViewModel.gameStateLive.observe(viewLifecycleOwner, {
            println("GameControlsFragment.onCreate gameStateViewModel.gameStateLive.observe")
        })

        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        updatePlayButton()
    }

    override fun onStop() {
        super.onStop()
        gameStateViewModel.stopTimer()
    }

    fun updatePlayButton() {
        if(gameStateViewModel.gameStateLive.value==null || gameStateViewModel.gameStateLive.value?.gameInProgress!!) {
            playPauseBtn.setIconResource(R.drawable.ic_baseline_play_circle_outline_24)
        } else {
            playPauseBtn.setIconResource(R.drawable.ic_baseline_pause_circle_outline_24)
        }
    }
}
