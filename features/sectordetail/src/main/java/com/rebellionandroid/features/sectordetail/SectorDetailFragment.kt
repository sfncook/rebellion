package com.rebellionandroid.features.sectordetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.database.gamestate.SectorWithPlanets
import com.rebllelionandroid.features.sectorsdetail.R
import kotlinx.coroutines.launch


class SectorDetailFragment: Fragment() {
    private var currentGameStateId: Long = 0
    private var selectedSectorId: Long = 0
    private lateinit var gameStateViewModel: GameStateViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_sector_detail, container, false)
        selectedSectorId = arguments?.getLong("sectorId")!!
        gameStateViewModel = (activity as BaseActivity).gameStateViewModel
        gameStateViewModel.gameState.observe(viewLifecycleOwner, { _gameStateWithSectors ->
            val selectedSector = _gameStateWithSectors.sectors.find { sectorWithPlanets ->
                sectorWithPlanets.sector.id == selectedSectorId
            }
            if (selectedSector != null) {
                updateSectorDetail(selectedSector)
            }
        })

        return root
    }

    override fun onResume() {
        super.onResume()

        gameStateViewModel.getSector(selectedSectorId) { sector ->
            val sectorName = sector.name
            viewLifecycleOwner.lifecycleScope.launch {
                (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Sector: $sectorName"
                (activity as AppCompatActivity?)!!.supportActionBar!!.subtitle = "Sector Detail"
                (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayShowHomeEnabled(true)
            }
        }

        val gameStateSharedPrefFile = getString(R.string.gameStateSharedPrefFile)
        val keyCurrentGameId = getString(R.string.keyCurrentGameId)
        val sharedPref = activity?.getSharedPreferences(
            gameStateSharedPrefFile,
            Context.MODE_PRIVATE
        )
        if(sharedPref?.contains(keyCurrentGameId) == true) {
            currentGameStateId = sharedPref.getLong(keyCurrentGameId, 0)
            gameStateViewModel.loadAllGameStateWithSectors(currentGameStateId)
        } else {
            println("ERROR No current game ID foudn in shared preferences")
        }
    }

    private fun updateSectorDetail(sectorWithPlanets: SectorWithPlanets) {
        val planets = sectorWithPlanets.planets
        val sortedPlanets = planets.toSortedSet(Comparator { s1, s2 ->
            s1.planet.name.compareTo(s2.planet.name)
        })
        val viewAdapter = PlanetsListAdapter(ArrayList(sortedPlanets))
        val recyclerView = view?.findViewById<RecyclerView>(R.id.planets_list)
        viewLifecycleOwner.lifecycleScope.launch {
            recyclerView?.adapter = viewAdapter
        }
    }

}