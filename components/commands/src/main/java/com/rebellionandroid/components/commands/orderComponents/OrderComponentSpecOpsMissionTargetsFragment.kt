package com.rebellionandroid.components.commands.orderComponents

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.rebellionandroid.components.commands.R
import com.rebellionandroid.components.commands.enums.OrderDlgArgumentKeys
import com.rebellionandroid.components.commands.enums.OrderProcedures
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.gamestate.enums.MissionTargetType
import com.rebllelionandroid.core.database.gamestate.enums.MissionType
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import kotlinx.coroutines.launch

class OrderComponentSpecOpsMissionTargetsFragment(): OrderComponent() {

    private var personnelId: Long? = null
    private var selectedMissionTargetId: Long? = null
    private lateinit var missionTargetBtnsList: LinearLayout
    private val missionTargetIdsToBtns = mutableMapOf<Long, Pair<MaterialButton, MissionTargetType>>()
    private lateinit var gameStateViewModel: GameStateViewModel
    private var suppressUpdate:Boolean = false
    private var currentGameStateId: Long? = null

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun newInstance(arguments: Bundle?): OrderComponentSpecOpsMissionTargetsFragment {
            val frag = OrderComponentSpecOpsMissionTargetsFragment()
            if(arguments!=null) {
                frag.arguments = arguments.deepCopy()
            } else {
                println("ERROR: arguments null for OrderComponentSpecOpsMissionTargetsFragment.newInstance")
            }
            return frag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_order_component_specops_mission_targets, container, false)

        missionTargetBtnsList = root.findViewById(R.id.missiontargets_btns)
        gameStateViewModel = (activity as BaseActivity).gameStateViewModel

        personnelId = arguments?.getLong(OrderDlgArgumentKeys.PersonnelId.value)
        if(personnelId==null) {
            println("ERROR: personnelId is nul for OrderComponentSpecOpsMissionTargetsFragment")
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        currentGameStateId = Utilities.getCurrentGameStateId(
            getString(R.string.gameStateSharedPrefFile),
            getString(R.string.keyCurrentGameId),
            requireActivity()
        )
    }

    private fun setSelectedMissionTarget(missionTargetId: Long?) {
        selectedMissionTargetId = missionTargetId
        updateBtns()
        suppressUpdate = true
        notifyParentOfSelection()
    }

    private fun updateBtns() {
        missionTargetIdsToBtns.values.forEach { pair->
            val missionTargetBtn = pair.first
            missionTargetBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.purple_200);
        }
        if(selectedMissionTargetId!=null) {
            val pair = missionTargetIdsToBtns.get(selectedMissionTargetId)
            val missionTargetBtn = pair?.first
            missionTargetBtn?.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.purple_700);
        }
    }

    private fun addButton(
        btnText: String,
        missionTargetId: Long,
        missionTargetType: MissionTargetType
    ) {
        val btn = MaterialButton(requireContext())
        btn.text = btnText
        missionTargetBtnsList.addView(btn)
        missionTargetIdsToBtns.put(missionTargetId, Pair(btn, missionTargetType))
        btn.setOnClickListener { setSelectedMissionTarget(missionTargetId) }
    }

    private fun addTargetBtnsForSabotage(targetPlanetWithUnits: PlanetWithUnits, myTeam: TeamLoyalty) {
        targetPlanetWithUnits.factories.forEach { factory ->
            if(factory.team != myTeam) {
                addButton(factory.factoryType.value, factory.id, MissionTargetType.Factory)
            }
        }

        targetPlanetWithUnits.shipsWithUnits.forEach { shipWithUnits ->
            if(shipWithUnits.ship.team != myTeam) {
                addButton(shipWithUnits.ship.shipType.value, shipWithUnits.ship.id, MissionTargetType.Ship)
            }
        }

        targetPlanetWithUnits.defenseStructures.forEach { structure ->
            if(Utilities.getPlanetLoyalty(targetPlanetWithUnits.planet) != myTeam) {
                addButton(structure.defenseStructureType.value, structure.id, MissionTargetType.DefenseStructure)
            }
        }
    }

    override fun getSelectedValue(): Map<String, String?> {
        val pair = missionTargetIdsToBtns[selectedMissionTargetId]
        return mapOf(
            OrderDlgArgumentKeys.MissionTargetId.value to selectedMissionTargetId?.toString(),
            OrderDlgArgumentKeys.MissionTargetType.value to pair?.second.toString()
        )
    }

    override fun setAllOrderParameters(orderParameters: Map<String, String?>) {
        if(!suppressUpdate) {
            missionTargetBtnsList.removeAllViews()
            val selectedMissionTypeStr = orderParameters[OrderDlgArgumentKeys.MissionType.value]
            if (selectedMissionTypeStr != null) {
                val selectedMissionType = MissionType.valueOf(selectedMissionTypeStr)
                gameStateViewModel.getPersonnel(personnelId!!) { personnel ->
                    gameStateViewModel.getPlanetWithUnits(personnel.locationPlanetId!!) { targetPlanetWithUnits ->
                        if(currentGameStateId!=null) {
                            val gameState = gameStateViewModel.getGameState(currentGameStateId!!)
                            viewLifecycleOwner.lifecycleScope.launch {
                                when (selectedMissionType) {
                                    MissionType.Sabotage -> addTargetBtnsForSabotage(targetPlanetWithUnits, gameState.myTeam)
                                    else -> println("unsupported mission type")
                                }
                                updateBtns()
                            }
                        }
                    } // getPlanetWithUnits
                } // getPersonnel
            }
        }
        suppressUpdate = false
    }
}