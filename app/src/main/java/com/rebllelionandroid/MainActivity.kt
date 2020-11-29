package com.rebllelionandroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rebellionandroid.features.newgameactivity.NewGameActivity
import com.rebellionandroid.features.sectorslist.SectorsListActivity
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        initAppDependencyInjection()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Stop timer on app start
        gameStateViewModel.stopTimer()
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

        // Look for currently-active game or show NewGameActivity
        val gameStateSharedPrefFile = getString(R.string.gameStateSharedPrefFile)
        val keyCurrentGameId = getString(R.string.keyCurrentGameId)
        val sharedPref = getSharedPreferences(gameStateSharedPrefFile, Context.MODE_PRIVATE)
        if(sharedPref.contains(keyCurrentGameId)) {
            startActivity(Intent(applicationContext, SectorsListActivity::class.java))
        } else {
            startActivity(Intent(applicationContext, NewGameActivity::class.java))
        }
    }
}
