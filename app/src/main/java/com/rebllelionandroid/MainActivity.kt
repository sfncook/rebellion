package com.rebllelionandroid

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rebellionandroid.features.newgameactivity.NewGameActivity
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.GameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var gameStateComponent: GameStateComponent
    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        initAppDependencyInjection()
        super.onCreate(savedInstanceState)
        println("MainActivity.onCreate")

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)

        val gameStateViewModel = gameStateComponent.gameStateViewModel()
        mainScope.launch(Dispatchers.IO) {
            if(gameStateViewModel.getGameState() == null) {
                val intent = Intent(applicationContext, NewGameActivity::class.java)
                startActivity(intent)
            }
        }

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_sectorslist))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        println("MainActivity.onStart")
//        gameTimerViewModel.startTimer()
    }

    override fun onStop() {
        super.onStop()
        println("MainActivity.onStop")
//        gameTimerViewModel.stopTimer()
    }

    override fun onPostResume() {
        super.onPostResume()
        println("MainActivity.onPostResume")
    }

    private fun initAppDependencyInjection() {
        gameStateComponent = DaggerGameStateComponent
            .builder()
            .contextModule(ContextModule(applicationContext))
            .build()
    }
}