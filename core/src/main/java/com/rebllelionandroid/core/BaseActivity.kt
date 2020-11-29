package com.rebllelionandroid.core

import androidx.appcompat.app.AppCompatActivity
import com.rebllelionandroid.core.di.GameStateComponent

abstract class BaseActivity: AppCompatActivity() {
    lateinit var gameStateComponent: GameStateComponent
    lateinit var gameStateViewModel: GameStateViewModel
}