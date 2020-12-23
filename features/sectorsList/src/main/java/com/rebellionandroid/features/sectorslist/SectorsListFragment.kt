package com.rebellionandroid.features.sectorslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.rebellionandroid.components.sectorWithPlanetsFragment.SectorListAdapter
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.database.gamestate.GameStateWithSectors
import com.rebllelionandroid.features.sectorsList.R
import kotlinx.coroutines.launch

class SectorsListFragment: Fragment() {

    private var currentGameStateId: Long = 0
    private lateinit var gameStateWithSectors: LiveData<GameStateWithSectors>
    private lateinit var gameStateViewModel: GameStateViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        gameStateViewModel = (activity as BaseActivity).gameStateViewModel
        gameStateWithSectors = gameStateViewModel.gameState
        return inflater.inflate(R.layout.fragment_sectors_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameStateWithSectors.observe(viewLifecycleOwner , {
            updateSectorsList(it)
        })
    }

    override fun onResume() {
        super.onResume()

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Galactic Detail"
        (activity as AppCompatActivity?)!!.supportActionBar!!.subtitle = "All galactic sectors"

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

    private fun updateSectorsList(gameStateWithSectors: GameStateWithSectors) {
        val sectors = gameStateWithSectors.sectors
        val sortedSectors = sectors.toSortedSet(Comparator { s1, s2 ->
            s1.sector.name.compareTo(s2.sector.name)
        })
        val viewAdapter = SectorListAdapter(ArrayList(sortedSectors), gameStateWithSectors.gameState.myTeam) { sectorId ->
            val bundle = bundleOf("sectorId" to sectorId)
            this.view?.findNavController()?.navigate(R.id.sector_detail_graph, bundle)
        }
        val recyclerView = view?.findViewById<RecyclerView>(R.id.sectors_list)
        viewLifecycleOwner.lifecycleScope.launch {
            recyclerView?.adapter = viewAdapter
        }
    }

}
