package com.rebllelionandroid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rebellionandroid.features.newgameactivity.NewGameActivity
import com.rebellionandroid.features.sectorslist.SectorsListActivity
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateUpdater
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.GameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        mainScope.launch(Dispatchers.IO) {
            if(gameStateViewModel.getManyGameStates() == 0) {
                val intent = Intent(applicationContext, NewGameActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(applicationContext, SectorsListActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
