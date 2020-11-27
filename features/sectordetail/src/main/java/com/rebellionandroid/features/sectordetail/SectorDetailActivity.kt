package com.rebellionandroid.features.sectordetail

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule
import kotlinx.coroutines.MainScope

class SectorDetailActivity: Activity() {
    lateinit var gameStateViewModel: GameStateViewModel

//    lateinit var viewAdapter: SectorListAdapter
    private val mainScope = MainScope()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        initAppDependencyInjection()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sector_detail)

//        mainScope.launch(Dispatchers.IO) {
//            if(gameStateViewModel.getManyGameStates()>0) {
//                viewAdapter = SectorListAdapter(gameStateViewModel.getCurrentGameStateWithSectors().sectors)
//                recyclerView = findViewById(R.id.sectors_list)
//                mainScope.launch(Dispatchers.Main) {
//                    recyclerView.adapter = viewAdapter
//                    viewBinding.sectorsList.apply {
//                        adapter = viewAdapter
//                    }
//                }
//            }
//        }
    }

    private fun initAppDependencyInjection() {
        val gameStateComponent = DaggerGameStateComponent
            .builder()
            .contextModule(ContextModule(applicationContext))
            .build()
        gameStateViewModel = gameStateComponent.gameStateViewModel()
    }
}