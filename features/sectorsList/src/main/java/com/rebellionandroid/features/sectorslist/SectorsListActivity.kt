package com.rebellionandroid.features.sectorslist

import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.database.gamestate.GameStateWithSectors
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
    lateinit var gameStateWithSectorsLive: LiveData<GameStateWithSectors>
    private var currentGameStateId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        initAppDependencyInjection()
        super.onCreate(savedInstanceState)

        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_sectors_list)
        viewBinding.viewModel = gameStateViewModel

    }

    override fun onResume() {
        super.onResume()

        val gameStateSharedPrefFile = getString(R.string.gameStateSharedPrefFile)
        val keyCurrentGameId = getString(R.string.keyCurrentGameId)
        val sharedPref = getSharedPreferences(gameStateSharedPrefFile, Context.MODE_PRIVATE)
        if(sharedPref.contains(keyCurrentGameId)) {
            currentGameStateId = sharedPref.getLong(keyCurrentGameId, 0)
            val lifecycleOwner = this
            mainScope.launch(Dispatchers.IO) {
                gameStateWithSectorsLive = gameStateViewModel.getGameStateWithSectorsLive(currentGameStateId)
                mainScope.launch(Dispatchers.Main) {
                    gameStateWithSectorsLive.observe(lifecycleOwner , {
                        updateSectorsList(it)
                    })
                }
            }
        } else {
            println("ERROR No current game ID foudn in shared preferences")
        }
    }

    private fun updateSectorsList(gameStateWithSectors: GameStateWithSectors) {
        val sectors = gameStateWithSectors.sectors
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
    }

    private fun initAppDependencyInjection() {
        gameStateComponent = DaggerGameStateComponent
            .builder()
            .contextModule(ContextModule(applicationContext))
            .build()
        gameStateViewModel = gameStateComponent.gameStateViewModel()
    }
}