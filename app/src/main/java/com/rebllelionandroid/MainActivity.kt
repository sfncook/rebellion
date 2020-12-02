package com.rebllelionandroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.rebellionandroid.features.newgameactivity.NewGameActivity
import com.rebellionandroid.features.sectorslist.SectorsListActivity
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule

class MainActivity : BaseActivity() {

    var fragDash: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        initAppDependencyInjection()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_dashboard, R.id.second_graph))
        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)

        findViewById<MaterialButton>(R.id.toggle_btn).setOnClickListener {
            if(fragDash) {
                navController.navigate(R.id.second_graph)
            } else {
                navController.navigate(R.id.navigation_dashboard)
            }
            fragDash = !fragDash
        }

        findViewById<MaterialButton>(R.id.btn_new_game).setOnClickListener {
            navController.navigate(R.id.navigation_newgame)
        }
    }

    private fun initAppDependencyInjection() {
        gameStateComponent = DaggerGameStateComponent
            .builder()
            .contextModule(ContextModule(applicationContext))
            .build()
        gameStateViewModel = gameStateComponent.gameStateViewModel()
    }

//    override fun onResume() {
//        super.onResume()
//        val gameStateSharedPrefFile = getString(R.string.gameStateSharedPrefFile)
//        val keyCurrentGameId = getString(R.string.keyCurrentGameId)
//        val sharedPref = getSharedPreferences(gameStateSharedPrefFile, Context.MODE_PRIVATE)
//        if(sharedPref.contains(keyCurrentGameId)) {
//            gameStateViewModel.stopTimer(sharedPref.getLong(keyCurrentGameId, 0))
//            startActivity(Intent(applicationContext, SectorsListActivity::class.java))
//        } else {
//            startActivity(Intent(applicationContext, NewGameActivity::class.java))
//        }
//    }
}
