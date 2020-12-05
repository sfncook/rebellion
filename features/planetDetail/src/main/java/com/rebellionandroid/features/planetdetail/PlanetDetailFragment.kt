package com.rebellionandroid.features.planetdetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.GameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule
import kotlinx.coroutines.MainScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.gamestate.SectorWithPlanets
import kotlinx.coroutines.launch

class PlanetDetailFragment: Fragment() {
    private var currentGameStateId: Long = 0
    private var selectedPlanetId: Long = 0
    private lateinit var gameStateViewModel: GameStateViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_planet_detail, container, false)
        selectedPlanetId = arguments?.getLong("planetId")!!

        gameStateViewModel = (activity as BaseActivity).gameStateViewModel
        val gameStateWithSectors = gameStateViewModel.gameState
        gameStateWithSectors.observe(viewLifecycleOwner , {
            val planetWithUnits = gameStateViewModel.getPlanetWithUnits(selectedPlanetId)
//            updatePlanetDetail(planetWithUnits)
        })

        return root
    }

    override fun onResume() {
        super.onResume()

        val gameStateSharedPrefFile = getString(R.string.gameStateSharedPrefFile)
        val keyCurrentGameId = getString(R.string.keyCurrentGameId)
        val sharedPref = activity?.getSharedPreferences(gameStateSharedPrefFile, Context.MODE_PRIVATE)
        if(sharedPref?.contains(keyCurrentGameId) == true) {
            currentGameStateId = sharedPref.getLong(keyCurrentGameId, 0)
            gameStateViewModel.loadAllGameStateWithSectors(currentGameStateId)
        } else {
            println("ERROR No current game ID foudn in shared preferences")
        }
    }

//    private fun updatePlanetDetail(planetWithUnits: PlanetWithUnits) {
//        val units = planetWithUnits.units
//        val sortedUnits = units.toSortedSet(Comparator { s1, s2 ->
//            s1..name.compareTo(s2.planet.name)
//        })
//        val viewAdapter = PlanetsListAdapter(ArrayList(sortedPlanets))
//        val recyclerView = view?.findViewById<RecyclerView>(R.id.planets_list)
//        viewLifecycleOwner.lifecycleScope.launch {
//            recyclerView?.adapter = viewAdapter
//        }
//    }

}