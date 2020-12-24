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
import com.rebllelionandroid.core.database.gamestate.GameStateWithSectors
import com.rebllelionandroid.core.database.gamestate.ShipWithUnits
import kotlinx.coroutines.launch


class ShipMoveDialogFragment: DialogFragment() {

    private lateinit var rootContext: Context
    private lateinit var sectorsAndPlanetsExpandableList: ExpandableListView
    private var lastGroupExpandedPos: Int = -1
    private var selectedShipId: Long = 0
    private lateinit var selectedShipWithUnits: ShipWithUnits
    private var currentGameStateId: Long = 0
    private var currentGameTimeDay: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_ship_move, container, false)
        rootContext = root.context
        selectedShipId = arguments?.getLong("shipId")!!
        currentGameStateId = arguments?.getLong("currentGameStateId")!!

        root.findViewById<MaterialButton>(R.id.ship_move_close_btn).setOnClickListener {
            dismiss()
        }

        sectorsAndPlanetsExpandableList = root.findViewById(R.id.ship_move_list_sector_and_planets) as ExpandableListView

        val gameStateViewModel = (activity as BaseActivity).gameStateViewModel
        gameStateViewModel.gameState.observe(viewLifecycleOwner , { gameStateWithSectors ->
            currentGameTimeDay = gameStateWithSectors.gameState.gameTime
            gameStateViewModel.getShipWithUnits(selectedShipId) { shipWithUnits ->
                selectedShipWithUnits = shipWithUnits
                gameStateViewModel.getPlanetWithUnits(selectedShipWithUnits.ship.locationPlanetId) { planetWithUnits ->
                    val selectedSectorId = planetWithUnits.planet.sectorId
                    viewLifecycleOwner.lifecycleScope.launch {
                        updateSectorsList(gameStateWithSectors, selectedSectorId, selectedShipWithUnits)
                    }
                }
            }
        })

        sectorsAndPlanetsExpandableList.setOnGroupExpandListener { groupPosition ->
            if (lastGroupExpandedPos != -1 && groupPosition != lastGroupExpandedPos) {
                sectorsAndPlanetsExpandableList.collapseGroup(lastGroupExpandedPos);
            }
            lastGroupExpandedPos = groupPosition;
        }

        sectorsAndPlanetsExpandableList.setOnChildClickListener { parent, v, groupPosition, childPosition, planetId ->
            if(selectedShipWithUnits.ship.locationPlanetId != planetId) {
                gameStateViewModel.startShipJourneyToPlanet(selectedShipId, planetId, currentGameStateId, currentGameTimeDay.plus(10))
                dismiss()
            }
            true
        }

        return root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun updateSectorsList(
        gameStateWithSectors: GameStateWithSectors,
        selectedSectorId: Long,
        selectedShipWithUnits: ShipWithUnits
    ) {
        val sectors = gameStateWithSectors.sectors
        val sortedSectors = sectors.toSortedSet(Comparator { s1, s2 ->
            s1.sector.name.compareTo(s2.sector.name)
        })
        val sectorsAndPlanetsListAdapter = SectorsAndPlanetsListAdapter(
            rootContext,
            sortedSectors.toList(),
            selectedShipWithUnits
        )
        sectorsAndPlanetsExpandableList.setAdapter(sectorsAndPlanetsListAdapter)

        // Expand selected sector
        val selectedSector = sortedSectors.find { sectorWithPlanets -> sectorWithPlanets.sector.id == selectedSectorId }
        val indexOfSelectedSector = sortedSectors.indexOf(selectedSector)
        sectorsAndPlanetsExpandableList.expandGroup(indexOfSelectedSector)
    }
}
