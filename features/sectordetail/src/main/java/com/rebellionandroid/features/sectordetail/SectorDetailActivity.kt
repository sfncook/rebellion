package com.rebellionandroid.features.sectordetail

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule
import com.rebllelionandroid.features.sectorsdetail.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SectorDetailActivity: Activity() {
    lateinit var gameStateViewModel: GameStateViewModel

    lateinit var viewAdapter: PlanetsListAdapter
    private val mainScope = MainScope()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        initAppDependencyInjection()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sector_detail)

        mainScope.launch(Dispatchers.IO) {
            if(gameStateViewModel.getManyGameStates()>0) {
                val selectedSectorId = intent.getLongExtra("SELECTED_SECTOR_ID", 0)
                val sectorWithPlanets = gameStateViewModel.getSectorWithPlanets(selectedSectorId)
                viewAdapter = PlanetsListAdapter(sectorWithPlanets.planets)
                recyclerView = findViewById(R.id.planets_list)
                mainScope.launch(Dispatchers.Main) {
                    recyclerView.adapter = viewAdapter
                }
            }
        }
    }

    private fun initAppDependencyInjection() {
        val gameStateComponent = DaggerGameStateComponent
            .builder()
            .contextModule(ContextModule(applicationContext))
            .build()
        gameStateViewModel = gameStateComponent.gameStateViewModel()
    }
}