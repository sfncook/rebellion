package com.rebllelionandroid

import android.os.Bundle
import android.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule

class MainActivity : BaseActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        initAppDependencyInjection()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.main_toolbar))
        navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard,
                R.id.sectors_list_graph,
                R.id.sector_detail_graph,
                R.id.planet_detail_graph,
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun initAppDependencyInjection() {
        gameStateComponent = DaggerGameStateComponent
            .builder()
            .contextModule(ContextModule(applicationContext))
            .build()
        gameStateViewModel = gameStateComponent.gameStateViewModel()
    }

    override fun onBackPressed() {
        val curDest = navController.currentDestination
        if(curDest?.id != R.id.fragment_sectors_list) {
            super.onBackPressed()
        } else {
            // Do nothing - Sectors list has no back behavior
        }
    }
}
