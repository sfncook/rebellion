package com.rebellionandroid.components.commands

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.database.gamestate.Factory
import com.rebllelionandroid.core.database.gamestate.GameStateWithSectors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FactoryBuildDialogFragment: DialogFragment() {

    private lateinit var rootContext: Context
    private lateinit var sectorsAndPlanetsExpandableList: ExpandableListView
    private var lastGroupExpandedPos: Int = -1
    private var selectedFactoryId: Long = 0
    private lateinit var selectedFactory: Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_factory_order, container, false)
        rootContext = root.context
        selectedFactoryId = arguments?.getLong("factoryId")!!

        root.findViewById<MaterialButton>(R.id.factmove_close_btn).setOnClickListener {
            dismiss()
        }

        sectorsAndPlanetsExpandableList = root.findViewById(R.id.factmove_list_sector_and_planets) as ExpandableListView

        val gameStateViewModel = (activity as BaseActivity).gameStateViewModel

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val gameStateSharedPrefFile = getString(R.string.gameStateSharedPrefFile)
            val keyCurrentGameId = getString(R.string.keyCurrentGameId)
            val sharedPref = activity?.getSharedPreferences(gameStateSharedPrefFile, Context.MODE_PRIVATE)
            if(sharedPref?.contains(keyCurrentGameId) == true) {
                val currentGameStateId = sharedPref.getLong(keyCurrentGameId, 0)
                gameStateViewModel.loadAllGameStateWithSectors(currentGameStateId)
                val gameStateWithSectors = gameStateViewModel.getGameStateWithSectors(currentGameStateId)
                selectedFactory = gameStateViewModel.getFactory(selectedFactoryId)
                val planet = gameStateViewModel.getPlanet(selectedFactory.locationPlanetId)
                viewLifecycleOwner.lifecycleScope.launch {
                    updateSectorsList(gameStateWithSectors, planet.sectorId)
                }
            } else {
                println("ERROR No current game ID foudn in shared preferences")
            }
        }

        sectorsAndPlanetsExpandableList.setOnGroupExpandListener { groupPosition ->
            if (lastGroupExpandedPos != -1 && groupPosition != lastGroupExpandedPos) {
                sectorsAndPlanetsExpandableList.collapseGroup(lastGroupExpandedPos);
            }
            lastGroupExpandedPos = groupPosition;
        }

        sectorsAndPlanetsExpandableList.setOnChildClickListener { _, _, _, _, planetId ->
//            if(selectedShipWithUnits.ship.locationPlanetId != planetId) {
//                gameStateViewModel.startShipJourneyToPlanet(selectedShipId, planetId, currentGameStateId)
//                dismiss()
//            }
            true
        }

        return root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun updateSectorsList(gameStateWithSectors: GameStateWithSectors, selectedSectorId: Long) {
        val sectors = gameStateWithSectors.sectors
        val sortedSectors = sectors.toSortedSet(Comparator { s1, s2 ->
            s1.sector.name.compareTo(s2.sector.name)
        })
        val sectorsAndPlanetsListAdapter = SectorsAndPlanetsListAdapter(rootContext, sortedSectors.toList())
        sectorsAndPlanetsExpandableList.setAdapter(sectorsAndPlanetsListAdapter)

        // Expand selected sector
        val selectedSector = sortedSectors.find { sectorWithPlanets -> sectorWithPlanets.sector.id == selectedSectorId }
        val indexOfSelectedSector = sortedSectors.indexOf(selectedSector)
        sectorsAndPlanetsExpandableList.expandGroup(indexOfSelectedSector)
    }
}
