package com.rebellionandroid.components.gamecontrols

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.rebellionandroid.components.gamecontrols.databinding.FragmentGameControlsBinding
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule
import javax.inject.Inject

class GameControlsFragment: Fragment() {

    @Inject
    lateinit var gameStateViewModel: GameStateViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //https://developer.android.com/training/dependency-injection/dagger-multi-module
        val gameStateComponent = (activity as BaseActivity).gameStateComponent
        DaggerGameControlsComponent.factory().create(gameStateComponent).inject(this)

//        return inflater.inflate(R.layout.fragment_game_controls, container, false)
        val viewBinding: FragmentGameControlsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_controls, container, false)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        return viewBinding.root
    }

}
