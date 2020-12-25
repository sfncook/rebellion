package com.rebellionandroid.components.commands

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.database.gamestate.Factory
import com.rebllelionandroid.core.database.gamestate.GameStateWithSectors
import com.rebllelionandroid.core.database.gamestate.enums.FactoryBuildTargetType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FactoryBuildDialogFragment: DialogFragment() {

    private lateinit var rootContext: Context
    private lateinit var sectorsAndPlanetsExpandableList: ExpandableListView
    private var lastGroupExpandedPos: Int = -1
    private var selectedFactoryId: Long = 0
    private lateinit var selectedFactory: Factory
    private var selectedBuildTargetType = FactoryBuildTargetType.ConstructionYard

    private lateinit var buildBtnConstructionYard: MaterialButton
    private lateinit var buildBtnShipYard: MaterialButton
    private lateinit var buildBtnTrainingFacility: MaterialButton
    private lateinit var buildBtnOrbitalBattery: MaterialButton
    private lateinit var buildBtnPlanetaryShield: MaterialButton

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

        buildBtnConstructionYard = root.findViewById(R.id.factmove_build_constructionyard)
        buildBtnShipYard = root.findViewById(R.id.factmove_build_shipyard)
        buildBtnTrainingFacility = root.findViewById(R.id.factmove_build_trainingfacility)
        buildBtnOrbitalBattery = root.findViewById(R.id.factmove_build_orbitalbattery)
        buildBtnPlanetaryShield = root.findViewById(R.id.factmove_build_planetaryshield)
        updateBuildBtns(root.context)

        buildBtnConstructionYard.setOnClickListener {
            selectedBuildTargetType = FactoryBuildTargetType.ConstructionYard
            updateBuildBtns(root.context)
        }
        buildBtnShipYard.setOnClickListener {
            selectedBuildTargetType = FactoryBuildTargetType.ShipYard
            updateBuildBtns(root.context)
        }
        buildBtnTrainingFacility.setOnClickListener {
            selectedBuildTargetType = FactoryBuildTargetType.TrainingFacility
            updateBuildBtns(root.context)
        }
        buildBtnOrbitalBattery.setOnClickListener {
            selectedBuildTargetType = FactoryBuildTargetType.OrbitalBattery
            updateBuildBtns(root.context)
        }
        buildBtnPlanetaryShield.setOnClickListener {
            selectedBuildTargetType = FactoryBuildTargetType.PlanetaryShield
            updateBuildBtns(root.context)
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

    private fun updateBuildBtns(context: Context) {
        val unselectedColor = ContextCompat.getColor(context, R.color.list_item_bg)
        val unselectedTextColor = ContextCompat.getColor(context, R.color.purple_700)
        buildBtnConstructionYard.setBackgroundColor(unselectedColor)
        buildBtnShipYard.setBackgroundColor(unselectedColor)
        buildBtnTrainingFacility.setBackgroundColor(unselectedColor)
        buildBtnOrbitalBattery.setBackgroundColor(unselectedColor)
        buildBtnPlanetaryShield.setBackgroundColor(unselectedColor)

        buildBtnConstructionYard.setTextColor(unselectedTextColor)
        buildBtnShipYard.setTextColor(unselectedTextColor)
        buildBtnTrainingFacility.setTextColor(unselectedTextColor)
        buildBtnOrbitalBattery.setTextColor(unselectedTextColor)
        buildBtnPlanetaryShield.setTextColor(unselectedTextColor)

        val selectedColor = ContextCompat.getColor(context, R.color.purple_200)
        val selectedTextColor = ContextCompat.getColor(context, R.color.white)
        when(selectedBuildTargetType) {
            FactoryBuildTargetType.ConstructionYard -> buildBtnConstructionYard.setBackgroundColor(selectedColor)
            FactoryBuildTargetType.ShipYard -> buildBtnShipYard.setBackgroundColor(selectedColor)
            FactoryBuildTargetType.TrainingFacility -> buildBtnTrainingFacility.setBackgroundColor(selectedColor)
            FactoryBuildTargetType.OrbitalBattery -> buildBtnOrbitalBattery.setBackgroundColor(selectedColor)
            FactoryBuildTargetType.PlanetaryShield -> buildBtnPlanetaryShield.setBackgroundColor(selectedColor)
            else -> {}
        }
        when(selectedBuildTargetType) {
            FactoryBuildTargetType.ConstructionYard -> buildBtnConstructionYard.setTextColor(selectedTextColor)
            FactoryBuildTargetType.ShipYard -> buildBtnShipYard.setTextColor(selectedTextColor)
            FactoryBuildTargetType.TrainingFacility -> buildBtnTrainingFacility.setTextColor(selectedTextColor)
            FactoryBuildTargetType.OrbitalBattery -> buildBtnOrbitalBattery.setTextColor(selectedTextColor)
            FactoryBuildTargetType.PlanetaryShield -> buildBtnPlanetaryShield.setTextColor(selectedTextColor)
            else -> {}
        }
    }
}
