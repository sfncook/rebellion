package com.rebellionandroid.components.commands.orderComponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.getColor
import com.rebellionandroid.components.commands.OrdersDialogFragment
import com.rebellionandroid.components.commands.R
import com.rebellionandroid.components.commands.enums.OrderDlgArgumentKeys
import com.rebllelionandroid.core.database.gamestate.enums.MissionType

class OrderComponentSpecOpsMissionTypesFragment(): OrderComponent() {

    private var selectedMissionType: MissionType? = null
    private lateinit var buildBtnSabotage: Button
    private lateinit var buildBtnInsurrection: Button
    private lateinit var buildBtnDiplomacy: Button
    private lateinit var buildBtnIntel: Button

    companion object {
        fun newInstance(): OrderComponentSpecOpsMissionTypesFragment {
            return OrderComponentSpecOpsMissionTypesFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_order_component_specops_mission_types, container, false)

        buildBtnSabotage = root.findViewById(R.id.missiontypes_sabotage)
        buildBtnInsurrection = root.findViewById(R.id.missiontypes_insurrection)
        buildBtnDiplomacy = root.findViewById(R.id.missiontypes_diplomacy)
        buildBtnIntel = root.findViewById(R.id.missiontypes_intel)

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