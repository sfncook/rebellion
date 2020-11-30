package com.rebellionandroid.features.sectorslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.database.gamestate.GameStateWithSectors
import com.rebllelionandroid.features.sectorsList.R
import com.rebllelionandroid.features.sectorsList.databinding.ActivitySectorsListBinding
import kotlinx.coroutines.MainScope

class SectorsListFragment: Fragment() {
    lateinit var viewBinding: ActivitySectorsListBinding
    lateinit var viewAdapter: SectorListAdapter
    private val mainScope = MainScope()
    private lateinit var recyclerView: RecyclerView
    lateinit var gameStateWithSectorsLive: LiveData<GameStateWithSectors>
    private var currentGameStateId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_sectors_list, container, false)
//        val navController = findNavController()
//        navController.navigate(R.id.navigation_notifications)
        return root
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        initAppDependencyInjection()
//        super.onCreate(savedInstanceState)
//
//        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_sectors_list)
//        viewBinding.viewModel = gameStateViewModel
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        val gameStateSharedPrefFile = getString(R.string.gameStateSharedPrefFile)
//        val keyCurrentGameId = getString(R.string.keyCurrentGameId)
//        val sharedPref = getSharedPreferences(gameStateSharedPrefFile, Context.MODE_PRIVATE)
//        if(sharedPref.contains(keyCurrentGameId)) {
//            currentGameStateId = sharedPref.getLong(keyCurrentGameId, 0)
//            val lifecycleOwner = this
//            mainScope.launch(Dispatchers.IO) {
//                gameStateWithSectorsLive = gameStateViewModel.getGameStateWithSectorsLive(currentGameStateId)
//                mainScope.launch(Dispatchers.Main) {
//                    gameStateWithSectorsLive.observe(lifecycleOwner , {
//                        updateSectorsList(it)
//                    })
//                }
//            }
//        } else {
//            println("ERROR No current game ID foudn in shared preferences")
//        }
//    }
//
//    private fun updateSectorsList(gameStateWithSectors: GameStateWithSectors) {
//        val sectors = gameStateWithSectors.sectors
//        val sortedSectors = sectors.toSortedSet(Comparator { s1, s2 ->
//            s1.sector.name.compareTo(s2.sector.name)
//        })
//        viewAdapter = SectorListAdapter(ArrayList(sortedSectors))
//        recyclerView = viewBinding.root.findViewById(R.id.sectors_list)
//        mainScope.launch(Dispatchers.Main) {
//            recyclerView.adapter = viewAdapter
//            viewBinding.sectorsList.apply {
//                adapter = viewAdapter
//            }
//        }
//    }
//
//    private fun initAppDependencyInjection() {
//        gameStateComponent = DaggerGameStateComponent
//            .builder()
//            .contextModule(ContextModule(applicationContext))
//            .build()
//        gameStateViewModel = gameStateComponent.gameStateViewModel()
//    }
}