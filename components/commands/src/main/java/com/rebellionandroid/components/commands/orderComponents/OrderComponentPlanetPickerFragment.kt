package com.rebellionandroid.components.commands.orderComponents

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.lifecycle.lifecycleScope
import com.rebellionandroid.components.commands.R
import com.rebellionandroid.components.commands.SectorsAndPlanetsListAdapter
import com.rebellionandroid.components.commands.enums.OrderDlgArgumentKeys
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.GameStateWithSectors
import com.rebllelionandroid.core.database.gamestate.SectorWithPlanets
import kotlinx.coroutines.launch

class OrderComponentPlanetPickerFragment(): OrderComponent() {

    private lateinit var sectorsAndPlanetsExpandableList: ExpandableListView
    private lateinit var gameStateViewModel: GameStateViewModel
    private var currentGameStateId: Long? = null
    private var selectedPlanetId: Long? = null
    private var lastGroupExpandedPos: Int? = null
    private lateinit var rootContext: Context

    companion object {
        fun newInstance(): OrderComponentPlanetPickerFragment {
            return OrderComponentPlanetPickerFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(
            R.layout.fragment_order_component_planet_picker,
            container,
            false
        )

        selectedPlanetId = arguments?.getLong("selectedPlanetId")

        sectorsAndPlanetsExpandableList = root.findViewById(R.id.ship_move_list_sector_and_planets)
        gameStateViewModel = (activity as BaseActivity).gameStateViewModel
        rootContext = root.context
//        parentFragment use this to callback on select

        // Only one group expanded at a time
        sectorsAndPlanetsExpandableList.setOnGroupExpandListener { groupPosition ->
            if (lastGroupExpandedPos != null && groupPosition != lastGroupExpandedPos) {
                sectorsAndPlanetsExpandableList.collapseGroup(lastGroupExpandedPos!!);
            }
            lastGroupExpandedPos = groupPosition;
        }

        // planet selection event
        sectorsAndPlanetsExpandableList.setOnChildClickListener { parent, v, groupPosition, childPosition, planetId ->
            selectedPlanetId = planetId
            updateList()
            notifyParentOfSelection()
            true
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        val gameStateSharedPrefFile = getString(R.string.gameStateSharedPrefFile)
        val keyCurrentGameId = getString(R.string.keyCurrentGameId)
        currentGameStateId = Utilities.getCurrentGameStateId(
            getString(R.string.gameStateSharedPrefFile),
            getString(R.string.keyCurrentGameId),
            requireActivity()
        )
        updateList()
    }

    override fun getSelectedValue(): Pair<String, String>? {
        if(selectedPlanetId!=null) {
            return Pair(OrderDlgArgumentKeys.SelectedPlanetId.value, selectedPlanetId.toString())
        } else {
            return null
        }

    }

    private fun getSortedSectorsList(gameStateWithSectors: GameStateWithSectors): List<SectorWithPlanets> {
        val sectors = gameStateWithSectors.sectors
        val sortedSectors = sectors.toSortedSet(Comparator { s1, s2 ->
            s1.sector.name.compareTo(s2.sector.name)
        })
        return sortedSectors.toList()
    }

    private fun updateList() {
        if(currentGameStateId!=null) {
            gameStateViewModel.getGameStateWithSectors(currentGameStateId!!) { gameStateWithSectors ->
                viewLifecycleOwner.lifecycleScope.launch {
                    updateSectorsList(gameStateWithSectors)
                    expandSectorForSelectedPlanet(gameStateWithSectors)
                }
            }
        }
    }

    private fun updateSectorsList(gameStateWithSectors: GameStateWithSectors) {
        val sortedSectors = getSortedSectorsList(gameStateWithSectors)
        val sectorsAndPlanetsListAdapter = SectorsAndPlanetsListAdapter(
            rootContext,
            sortedSectors.toList(),
            selectedPlanetId = selectedPlanetId
        )
        sectorsAndPlanetsExpandableList.setAdapter(sectorsAndPlanetsListAdapter)
    }

    private fun expandSectorForSelectedPlanet(gameStateWithSectors: GameStateWithSectors) {
        val sortedSectors = getSortedSectorsList(gameStateWithSectors)
        val selectedSector = sortedSectors.find { sectorWithPlanets ->
            sectorWithPlanets.planets.any { planetWithUnits ->
                planetWithUnits.planet.id == selectedPlanetId
            }
        }
        if(selectedSector!=null) {
            val indexOfSelectedSector = sortedSectors.indexOf(selectedSector)
            sectorsAndPlanetsExpandableList.expandGroup(indexOfSelectedSector)
        }
    }

    override fun setAllOrderParameters(orderParameters: Map<String, String>) {
        // Do nothing
    }
}