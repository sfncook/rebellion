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
import com.rebllelionandroid.core.database.gamestate.enums.FactoryType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FactoryBuildDialogFragment: DialogFragment() {

    private lateinit var rootContext: Context
    private lateinit var sectorsAndPlanetsExpandableList: ExpandableListView
    private var lastGroupExpandedPos: Int = -1
    private var selectedFactoryId: Long = 0
    private lateinit var selectedFactory: Factory
    private var selectedBuildTargetType: FactoryBuildTargetType? = null

    // Structures
    private lateinit var buildBtnConstructionYard: MaterialButton
    private lateinit var buildBtnShipYard: MaterialButton
    private lateinit var buildBtnTrainingFacility: MaterialButton
    private lateinit var buildBtnOrbitalBattery: MaterialButton
    private lateinit var buildBtnPlanetaryShield: MaterialButton
    // Ships
    private lateinit var buildBtnShip2: MaterialButton
    private lateinit var buildBtnShip3: MaterialButton
    private lateinit var buildBtnShip4: MaterialButton
    private lateinit var buildBtnShip5: MaterialButton
    private lateinit var buildBtnShip6: MaterialButton
    private lateinit var buildBtnShip7: MaterialButton
    private lateinit var buildBtnShip8: MaterialButton
    // Personelle
    private lateinit var buildBtnShipGarrison: MaterialButton
    private lateinit var buildBtnShipSpecOps: MaterialButton

    private lateinit var containerBtnsConstructionYard: View
    private lateinit var containerBtnsShipYard: View
    private lateinit var containerBtnsTrainingFacility: View

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
        containerBtnsConstructionYard = root.findViewById(R.id.factmove_btns_constructionyard)
        containerBtnsShipYard = root.findViewById(R.id.factmove_btns_shipyard)
        containerBtnsTrainingFacility = root.findViewById(R.id.factmove_btns_trainingfacility)

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

                    // Update build buttons
                    containerBtnsConstructionYard.visibility = View.GONE
                    containerBtnsShipYard.visibility = View.GONE
                    containerBtnsTrainingFacility.visibility = View.GONE
                    when(selectedFactory.factoryType) {
                        FactoryType.ConstructionYard -> containerBtnsConstructionYard.visibility = View.VISIBLE
                        FactoryType.ShipYard -> containerBtnsShipYard.visibility = View.VISIBLE
                        FactoryType.TrainingFaciliy -> containerBtnsTrainingFacility.visibility = View.VISIBLE
                        else -> {}
                    }
                }
            } else {
                println("ERROR No current game ID foudn in shared preferences")
            }
        }

        // Only one group expanded at a time
        sectorsAndPlanetsExpandableList.setOnGroupExpandListener { groupPosition ->
            if (lastGroupExpandedPos != -1 && groupPosition != lastGroupExpandedPos) {
                sectorsAndPlanetsExpandableList.collapseGroup(lastGroupExpandedPos)
            }
            lastGroupExpandedPos = groupPosition
        }

        // planet selection event
        sectorsAndPlanetsExpandableList.setOnChildClickListener { _, _, _, _, planetId ->
            if(selectedBuildTargetType!=null) {
                val gameStateSharedPrefFile = getString(R.string.gameStateSharedPrefFile)
                val keyCurrentGameId = getString(R.string.keyCurrentGameId)
                val sharedPref = activity?.getSharedPreferences(gameStateSharedPrefFile, Context.MODE_PRIVATE)
                if(sharedPref?.contains(keyCurrentGameId) == true) {
                    val currentGameStateId = sharedPref.getLong(keyCurrentGameId, 0)
                    gameStateViewModel.setFactoryBuildOrder(selectedFactoryId, planetId, selectedBuildTargetType!!, currentGameStateId)
                } else {
                    println("ERROR No current game ID found in shared preferences")
                }
                dismiss()
            }
            true
        }

        buildBtnConstructionYard = root.findViewById(R.id.factmove_build_constructionyard)
        buildBtnShipYard = root.findViewById(R.id.factmove_build_shipyard)
        buildBtnTrainingFacility = root.findViewById(R.id.factmove_build_trainingfacility)
        buildBtnOrbitalBattery = root.findViewById(R.id.factmove_build_orbitalbattery)
        buildBtnPlanetaryShield = root.findViewById(R.id.factmove_build_planetaryshield)

        buildBtnShip2 = root.findViewById(R.id.factmove_build_2)
        buildBtnShip3 = root.findViewById(R.id.factmove_build_3)
        buildBtnShip4 = root.findViewById(R.id.factmove_build_4)
        buildBtnShip5 = root.findViewById(R.id.factmove_build_5)
        buildBtnShip6 = root.findViewById(R.id.factmove_build_6)
        buildBtnShip7 = root.findViewById(R.id.factmove_build_7)
        buildBtnShip8 = root.findViewById(R.id.factmove_build_8)

        buildBtnShipGarrison = root.findViewById(R.id.factmove_build_garrison)
        buildBtnShipSpecOps = root.findViewById(R.id.factmove_build_specops)

        updateBuildBtns(root.context)

        buildBtnConstructionYard.setOnClickListener {selectBuildTargetType(FactoryBuildTargetType.ConstructionYard_ConstructionYard)}
        buildBtnShipYard.setOnClickListener {selectBuildTargetType(FactoryBuildTargetType.ConstructionYard_ShipYard)}
        buildBtnTrainingFacility.setOnClickListener {selectBuildTargetType(FactoryBuildTargetType.ConstructionYard_TrainingFacility)}
        buildBtnOrbitalBattery.setOnClickListener {selectBuildTargetType(FactoryBuildTargetType.ConstructionYard_OrbitalBattery)}
        buildBtnPlanetaryShield.setOnClickListener {selectBuildTargetType(FactoryBuildTargetType.ConstructionYard_PlanetaryShield)}

        buildBtnShip2.setOnClickListener {selectBuildTargetType(FactoryBuildTargetType.ShipYard_Bireme)}
        buildBtnShip3.setOnClickListener {selectBuildTargetType(FactoryBuildTargetType.ShipYard_Trireme)}
        buildBtnShip4.setOnClickListener {selectBuildTargetType(FactoryBuildTargetType.ShipYard_Quadrireme)}
        buildBtnShip5.setOnClickListener {selectBuildTargetType(FactoryBuildTargetType.ShipYard_Quinquereme)}
        buildBtnShip6.setOnClickListener {selectBuildTargetType(FactoryBuildTargetType.ShipYard_Hexareme)}
        buildBtnShip7.setOnClickListener {selectBuildTargetType(FactoryBuildTargetType.ShipYard_Septireme)}
        buildBtnShip8.setOnClickListener {selectBuildTargetType(FactoryBuildTargetType.ShipYard_Octere)}

        buildBtnShipGarrison.setOnClickListener {selectBuildTargetType(FactoryBuildTargetType.TrainingFac_Garrison)}
        buildBtnShipSpecOps.setOnClickListener {selectBuildTargetType(FactoryBuildTargetType.TrainingFac_SpecOps)}

        return root
    }

    private fun selectBuildTargetType(factoryBuildTargetType: FactoryBuildTargetType) {
        selectedBuildTargetType = factoryBuildTargetType
        view?.let { updateBuildBtns(it.context) }
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
        buildBtnShip2.setBackgroundColor(unselectedColor)
        buildBtnShip3.setBackgroundColor(unselectedColor)
        buildBtnShip4.setBackgroundColor(unselectedColor)
        buildBtnShip5.setBackgroundColor(unselectedColor)
        buildBtnShip6.setBackgroundColor(unselectedColor)
        buildBtnShip7.setBackgroundColor(unselectedColor)
        buildBtnShip8.setBackgroundColor(unselectedColor)
        buildBtnShipGarrison.setBackgroundColor(unselectedColor)
        buildBtnShipSpecOps.setBackgroundColor(unselectedColor)


        buildBtnConstructionYard.setTextColor(unselectedTextColor)
        buildBtnShipYard.setTextColor(unselectedTextColor)
        buildBtnTrainingFacility.setTextColor(unselectedTextColor)
        buildBtnOrbitalBattery.setTextColor(unselectedTextColor)
        buildBtnPlanetaryShield.setTextColor(unselectedTextColor)
        buildBtnShip2.setTextColor(unselectedTextColor)
        buildBtnShip3.setTextColor(unselectedTextColor)
        buildBtnShip4.setTextColor(unselectedTextColor)
        buildBtnShip5.setTextColor(unselectedTextColor)
        buildBtnShip6.setTextColor(unselectedTextColor)
        buildBtnShip7.setTextColor(unselectedTextColor)
        buildBtnShip8.setTextColor(unselectedTextColor)
        buildBtnShipGarrison.setTextColor(unselectedTextColor)
        buildBtnShipSpecOps.setTextColor(unselectedTextColor)

        val selectedColor = ContextCompat.getColor(context, R.color.purple_200)
        val selectedTextColor = ContextCompat.getColor(context, R.color.white)
        when(selectedBuildTargetType) {
            FactoryBuildTargetType.ConstructionYard_ConstructionYard -> buildBtnConstructionYard.setBackgroundColor(selectedColor)
            FactoryBuildTargetType.ConstructionYard_ShipYard -> buildBtnShipYard.setBackgroundColor(selectedColor)
            FactoryBuildTargetType.ConstructionYard_TrainingFacility -> buildBtnTrainingFacility.setBackgroundColor(selectedColor)
            FactoryBuildTargetType.ConstructionYard_OrbitalBattery -> buildBtnOrbitalBattery.setBackgroundColor(selectedColor)
            FactoryBuildTargetType.ConstructionYard_PlanetaryShield -> buildBtnPlanetaryShield.setBackgroundColor(selectedColor)

            FactoryBuildTargetType.ShipYard_Bireme -> buildBtnShip2.setBackgroundColor(selectedColor)
            FactoryBuildTargetType.ShipYard_Trireme -> buildBtnShip3.setBackgroundColor(selectedColor)
            FactoryBuildTargetType.ShipYard_Quadrireme -> buildBtnShip4.setBackgroundColor(selectedColor)
            FactoryBuildTargetType.ShipYard_Quinquereme -> buildBtnShip5.setBackgroundColor(selectedColor)
            FactoryBuildTargetType.ShipYard_Hexareme -> buildBtnShip6.setBackgroundColor(selectedColor)
            FactoryBuildTargetType.ShipYard_Septireme -> buildBtnShip7.setBackgroundColor(selectedColor)
            FactoryBuildTargetType.ShipYard_Octere -> buildBtnShip8.setBackgroundColor(selectedColor)

            FactoryBuildTargetType.TrainingFac_Garrison -> buildBtnShipGarrison.setBackgroundColor(selectedColor)
            FactoryBuildTargetType.TrainingFac_SpecOps -> buildBtnShipSpecOps.setBackgroundColor(selectedColor)
            else -> {}
        }
        when(selectedBuildTargetType) {
            FactoryBuildTargetType.ConstructionYard_ConstructionYard -> buildBtnConstructionYard.setTextColor(selectedTextColor)
            FactoryBuildTargetType.ConstructionYard_ShipYard -> buildBtnShipYard.setTextColor(selectedTextColor)
            FactoryBuildTargetType.ConstructionYard_TrainingFacility -> buildBtnTrainingFacility.setTextColor(selectedTextColor)
            FactoryBuildTargetType.ConstructionYard_OrbitalBattery -> buildBtnOrbitalBattery.setTextColor(selectedTextColor)
            FactoryBuildTargetType.ConstructionYard_PlanetaryShield -> buildBtnPlanetaryShield.setTextColor(selectedTextColor)

            FactoryBuildTargetType.ShipYard_Bireme -> buildBtnPlanetaryShield.setTextColor(selectedTextColor)
            FactoryBuildTargetType.ShipYard_Trireme -> buildBtnPlanetaryShield.setTextColor(selectedTextColor)
            FactoryBuildTargetType.ShipYard_Quadrireme -> buildBtnPlanetaryShield.setTextColor(selectedTextColor)
            FactoryBuildTargetType.ShipYard_Quinquereme -> buildBtnPlanetaryShield.setTextColor(selectedTextColor)
            FactoryBuildTargetType.ShipYard_Hexareme -> buildBtnPlanetaryShield.setTextColor(selectedTextColor)
            FactoryBuildTargetType.ShipYard_Septireme -> buildBtnPlanetaryShield.setTextColor(selectedTextColor)
            FactoryBuildTargetType.ShipYard_Octere -> buildBtnPlanetaryShield.setTextColor(selectedTextColor)

            FactoryBuildTargetType.TrainingFac_Garrison -> buildBtnPlanetaryShield.setTextColor(selectedTextColor)
            FactoryBuildTargetType.TrainingFac_SpecOps -> buildBtnPlanetaryShield.setTextColor(selectedTextColor)
            else -> {}
        }
    }
}
