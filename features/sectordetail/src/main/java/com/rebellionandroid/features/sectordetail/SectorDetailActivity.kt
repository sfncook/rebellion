package com.rebellionandroid.features.sectordetail

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule
import com.rebllelionandroid.features.sectorsdetail.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SectorDetailActivity: BaseActivity() {

    lateinit var viewAdapter: PlanetsListAdapter
    private val mainScope = MainScope()
    private lateinit var recyclerView: RecyclerView
    var currentGameStateId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initAppDependencyInjection()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sector_detail)

        mainScope.launch(Dispatchers.IO) {
            val selectedSectorId = intent.getLongExtra("SELECTED_SECTOR_ID", 0)
            val sectorWithPlanets = gameStateViewModel.getSectorWithPlanets(selectedSectorId)
            val sector = sectorWithPlanets.sector
            val toolbar = findViewById<Toolbar>(R.id.sector_detail_toolbar)
            viewAdapter = PlanetsListAdapter(Utilities.sortPlanets(sectorWithPlanets.planets))
            recyclerView = findViewById(R.id.planets_list)
            mainScope.launch(Dispatchers.Main) {
                toolbar.title = "Sector: ${sector.name}"
                recyclerView.adapter = viewAdapter
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