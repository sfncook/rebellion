package com.rebellionandroid.components.commands.orderComponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.rebellionandroid.components.commands.R
import com.rebellionandroid.components.commands.enums.OrderDlgArgumentKeys
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.gamestate.enums.MissionType
import kotlinx.coroutines.launch

class OrderComponentSpecOpsMissionTargetsFragment(): OrderComponent() {

    private var selectedMissionTargetId: Long? = null
    private lateinit var missionTargetBtnsList: LinearLayout
    private val missionTargetIdsToBtns = mutableMapOf<Long, MaterialButton>()
    private lateinit var gameStateViewModel: GameStateViewModel
    private var suppressUpdate:Boolean = false

    companion object {
        fun newInstance(): OrderComponentSpecOpsMissionTargetsFragment {
            return OrderComponentSpecOpsMissionTargetsFragment()
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

        return root
    }

    private fun setSelectedMissionTarget(missionTargetId: Long?) {
        selectedMissionTargetId = missionTargetId
        updateBtns()
        suppressUpdate = true
        notifyParentOfSelection()
    }

    private fun updateBtns() {
        //materialButton.setBackgroundTintList(ContextCompat.getColorStateList(this@MyActivity, R.color.myCustomColor));
        missionTargetIdsToBtns.values.forEach {
            it.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.purple_200);
        }
        if(selectedMissionTargetId!=null) {
            val missionTargetBtn = missionTargetIdsToBtns.get(selectedMissionTargetId)
            missionTargetBtn?.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.purple_700);
        }
    }

    override fun getSelectedValue(): Pair<String, String>? {
        if(selectedMissionTargetId!=null) {
            return Pair(OrderDlgArgumentKeys.MissionTarget.value, selectedMissionTargetId.toString())
        } else {
            return null
        }
    }

    private fun addButton(
        btnText: String,
        missionTargetId: Long
    ) {
        val btn = MaterialButton(requireContext())
        btn.text = btnText
        missionTargetBtnsList.addView(btn)
        missionTargetIdsToBtns.put(missionTargetId, btn)
        btn.setOnClickListener { setSelectedMissionTarget(missionTargetId) }
    }

    private fun addTargetBtnsForSabotage(targetPlanetWithUnits: PlanetWithUnits) {
        targetPlanetWithUnits.factories.forEach { factory ->
            addButton(factory.factoryType.value, factory.id)
        }

        targetPlanetWithUnits.shipsWithUnits.forEach { shipWithUnits ->
            addButton(shipWithUnits.ship.shipType.value, shipWithUnits.ship.id)
        }

        targetPlanetWithUnits.defenseStructures.forEach { structure ->
            addButton(structure.defenseStructureType.value, structure.id)
        }
    }

    override fun setAllOrderParameters(orderParameters: Map<String, String>) {
        if(!suppressUpdate) {
            missionTargetBtnsList.removeAllViews()
            val selectedMissionTypeStr = orderParameters[OrderDlgArgumentKeys.MissionType.value]
            val selectedPlanetId = orderParameters[OrderDlgArgumentKeys.SelectedPlanetId.value]
            if (selectedMissionTypeStr != null && selectedPlanetId != null) {
                val selectedMissionType = MissionType.valueOf(selectedMissionTypeStr)
                gameStateViewModel.getPlanetWithUnits(selectedPlanetId.toLong()) { targetPlanetWithUnits ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        when (selectedMissionType) {
                            MissionType.Sabotage -> addTargetBtnsForSabotage(targetPlanetWithUnits)
                            else -> println("unsupported mission type")
                        }
                        updateBtns()
                    }
                }
            }
        }
        suppressUpdate = false
    }
}