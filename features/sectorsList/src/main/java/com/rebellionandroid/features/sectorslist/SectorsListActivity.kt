package com.rebellionandroid.features.sectorslist

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rebellionandroid.features.newgameactivity.NewGameActivity
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule
import com.rebllelionandroid.features.sectorsList.R
import com.rebllelionandroid.features.sectorsList.databinding.ActivitySectorsListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class SectorsListActivity: BaseActivity() {
    lateinit var viewBinding: ActivitySectorsListBinding
    lateinit var viewAdapter: SectorListAdapter
    private val mainScope = MainScope()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        initAppDependencyInjection()
        super.onCreate(savedInstanceState)

        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_sectors_list)
        viewBinding.viewModel = gameStateViewModel
    }

    override fun onResume() {
        super.onResume()

        gameStateViewModel.gameStateLive.observe(this, {
            println("gameStateViewModel.gameStateLive.observe")
            updateSectorsList()
        })
        updateSectorsList()
    }

    private fun updateSectorsList() {
        mainScope.launch(Dispatchers.IO) {
            if(gameStateViewModel.getManyGameStates()>0) {
                val sectors = gameStateViewModel.getCurrentGameStateWithSectors().sectors
                val sortedSectors = sectors.toSortedSet(Comparator { s1, s2 ->
                    s1.sector.name.compareTo(s2.sector.name)
                })
                viewAdapter = SectorListAdapter(ArrayList(sortedSectors))
                recyclerView = viewBinding.root.findViewById(R.id.sectors_list)
                mainScope.launch(Dispatchers.Main) {
                    recyclerView.adapter = viewAdapter
                    viewBinding.sectorsList.apply {
                        adapter = viewAdapter
                    }
                }
            } else {
                val intent = Intent(applicationContext, NewGameActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun initAppDependencyInjection() {
        gameStateComponent = DaggerGameStateComponent
            .builder()
            .contextModule(ContextModule(applicationContext))
            .build()
        gameStateViewModel = gameStateComponent.gameStateViewModel()
    }
}