package com.rebllelionandroid

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.GameTimer
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private var gameStateViewModel: GameStateViewModel = GameStateViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("MainActivity.onCreate")
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_sectorslist))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onStart() {
        super.onStart()
        println("MainActivity.onStart")
        gameStateViewModel.startTimer()
    }

    override fun onStop() {
        super.onStop()
        println("MainActivity.onStop")
        gameStateViewModel.stopTimer()
    }

    override fun onPostResume() {
        super.onPostResume()
        println("MainActivity.onPostResume")
    }
}