package com.rebellionandroid.components.commands.orderComponents

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getColor
import com.rebellionandroid.components.commands.OrdersDialogFragment
import com.rebellionandroid.components.commands.R
import com.rebellionandroid.components.commands.enums.OrderDlgArgumentKeys
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.Personnel
import com.rebllelionandroid.core.database.gamestate.Planet
import com.rebllelionandroid.core.database.gamestate.enums.MissionType

class OrderComponentSpecOpsMissionTypesFragment(): OrderComponent() {

    private lateinit var gameStateViewModel: GameStateViewModel
    private var selectedMissionType: MissionType? = null
    private lateinit var buildBtnSabotage: Button
    private lateinit var buildBtnInsurrection: Button
    private lateinit var buildBtnDiplomacy: Button
    private lateinit var buildBtnIntel: Button

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun newInstance(arguments: Bundle?): OrderComponentSpecOpsMissionTypesFragment {
            val frag = OrderComponentSpecOpsMissionTypesFragment()
            if(arguments!=null) {
                frag.arguments = arguments.deepCopy()
            } else {
                println("ERROR: arguments null for OrderComponentSpecOpsMissionTypesFragment.newInstance")
            }
            return frag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_order_component_specops_mission_types, container, false)

        gameStateViewModel = (activity as BaseActivity).gameStateViewModel

        buildBtnSabotage = root.findViewById(R.id.missiontypes_sabotage)
        buildBtnInsurrection = root.findViewById(R.id.missiontypes_insurrection)
        buildBtnDiplomacy = root.findViewById(R.id.missiontypes_diplomacy)
        buildBtnIntel = root.findViewById(R.id.missiontypes_intel)

        // Show/hide buttons relevant to planet location
        val personnelId = arguments?.getLong(OrderDlgArgumentKeys.PersonnelId.value)
        if(personnelId==null) {
            println("ERROR: personnelId is null for OrderComponentSpecOpsMissionTypesFragment")
        } else {
            gameStateViewModel.getPersonnel(personnelId) { personnel ->
                val targetPlanet: Planet?
                if(personnel.locationPlanetId!=null) {
                    targetPlanet = gameStateViewModel.getPlanet(personnel.locationPlanetId!!)
                } else {
                    val ship = gameStateViewModel.getShip(personnel.locationShip!!)
                    targetPlanet = gameStateViewModel.getPlanet(ship.locationPlanetId)
                }
                val targetPlanetLoyalty = Utilities.getPlanetLoyalty(targetPlanet)
                if(personnel.team==targetPlanetLoyalty) {
                    buildBtnSabotage.visibility = View.GONE
                    buildBtnInsurrection.visibility = View.GONE
                    buildBtnDiplomacy.visibility = View.VISIBLE
                    buildBtnIntel.visibility = View.VISIBLE
                } else {
                    buildBtnSabotage.visibility = View.VISIBLE
                    buildBtnInsurrection.visibility = View.VISIBLE
                    buildBtnDiplomacy.visibility = View.VISIBLE
                    buildBtnIntel.visibility = View.VISIBLE
                }
            }
        }

        buildBtnSabotage.setOnClickListener { setSelectedMissionType(MissionType.Sabotage) }
        buildBtnInsurrection.setOnClickListener { setSelectedMissionType(MissionType.Insurrection) }
        buildBtnDiplomacy.setOnClickListener { setSelectedMissionType(MissionType.Diplomacy) }
        buildBtnIntel.setOnClickListener { setSelectedMissionType(MissionType.Intelligence) }

        updateBtns()

        return root
    }

    private fun setSelectedMissionType(missionType: MissionType?) {
        selectedMissionType = missionType
        updateBtns()
        notifyParentOfSelection()
    }

    private fun updateBtns() {
        buildBtnSabotage.setBackgroundColor(getColor(requireContext(), R.color.purple_200))
        buildBtnInsurrection.setBackgroundColor(getColor(requireContext(), R.color.purple_200))
        buildBtnDiplomacy.setBackgroundColor(getColor(requireContext(), R.color.purple_200))
        buildBtnIntel.setBackgroundColor(getColor(requireContext(), R.color.purple_200))
        when(selectedMissionType) {
            MissionType.Sabotage -> buildBtnSabotage.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
            MissionType.Insurrection -> buildBtnInsurrection.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
            MissionType.Diplomacy -> buildBtnDiplomacy.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
            MissionType.Intelligence -> buildBtnIntel.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
        }
    }

    override fun getSelectedValue(): Map<String, String?> {
        return mapOf(OrderDlgArgumentKeys.MissionType.value to selectedMissionType?.value)
    }

    override fun setAllOrderParameters(orderParameters: Map<String, String?>) {
        // Do nothing
    }
}