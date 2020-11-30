package com.rebllelionandroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.rebellionandroid.features.newgameactivity.NewGameActivity
import com.rebellionandroid.features.sectorslist.SectorsListActivity
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initAppDependencyInjection()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun initAppDependencyInjection() {
        gameStateComponent = DaggerGameStateComponent
            .builder()
            .contextModule(ContextModule(applicationContext))
            .build()
        gameStateViewModel = gameStateComponent.gameStateViewModel()
    }

    override fun onResume() {
        super.onResume()
        val gameStateSharedPrefFile = getString(R.string.gameStateSharedPrefFile)
        val keyCurrentGameId = getString(R.string.keyCurrentGameId)
        val sharedPref = getSharedPreferences(gameStateSharedPrefFile, Context.MODE_PRIVATE)
        if(sharedPref.contains(keyCurrentGameId)) {
            gameStateViewModel.stopTimer(sharedPref.getLong(keyCurrentGameId, 0))
            startActivity(Intent(applicationContext, SectorsListActivity::class.java))
        } else {
            startActivity(Intent(applicationContext, NewGameActivity::class.java))
        }
    }
}
