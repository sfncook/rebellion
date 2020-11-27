package com.rebellionandroid.features.planetdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.GameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule
import kotlinx.coroutines.MainScope

class PlanetDetailActivity : AppCompatActivity() {

    lateinit var gameStateComponent: GameStateComponent
    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        initAppDependencyInjection()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planet_detail)

//        val gameStateViewModel = gameStateComponent.gameStateViewModel()
//        mainScope.launch(Dispatchers.IO) {
//            if(gameStateViewModel.getManyGameStates() == 0) {
//                val intent = Intent(applicationContext, NewGameActivity::class.java)
//                startActivity(intent)
//            } else {
//                val intent = Intent(applicationContext, SectorsListActivity::class.java)
//                startActivity(intent)
//            }
//        }
//
//        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//
//        val navController = findNavController(R.id.nav_host_fragment)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_sectorslist))
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
    }

    private fun initAppDependencyInjection() {
        gameStateComponent = DaggerGameStateComponent
            .builder()
            .contextModule(ContextModule(applicationContext))
            .build()
    }
}