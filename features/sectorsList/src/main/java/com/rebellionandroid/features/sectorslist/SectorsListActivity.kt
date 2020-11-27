package com.rebellionandroid.features.sectorslist

import android.app.Activity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule
import com.rebllelionandroid.features.sectorsList.R
import com.rebllelionandroid.features.sectorsList.databinding.ActivitySectorsListBinding
import com.rebllelionandroid.features.sectorsList.databinding.FragmentSectorsListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SectorsListActivity: Activity() {
    lateinit var gameStateViewModel: GameStateViewModel

    lateinit var viewBinding: ActivitySectorsListBinding
    lateinit var viewAdapter: SectorListAdapter
    private val mainScope = MainScope()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        initAppDependencyInjection()
        super.onCreate(savedInstanceState)

        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_sectors_list)
        viewBinding.viewModel = gameStateViewModel

        mainScope.launch(Dispatchers.IO) {
            if(gameStateViewModel.getManyGameStates()>0) {
                viewAdapter = SectorListAdapter(gameStateViewModel.getCurrentGameStateWithSectors().sectors)
                recyclerView = viewBinding.root.findViewById(R.id.sectors_list)
                mainScope.launch(Dispatchers.Main) {
                    recyclerView.adapter = viewAdapter
                    viewBinding.sectorsList.apply {
                        adapter = viewAdapter
                    }
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