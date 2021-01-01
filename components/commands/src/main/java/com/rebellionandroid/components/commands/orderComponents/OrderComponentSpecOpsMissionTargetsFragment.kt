package com.rebellionandroid.components.commands.orderComponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.getColor
import com.rebellionandroid.components.commands.R
import com.rebellionandroid.components.commands.enums.OrderDlgArgumentKeys

class OrderComponentSpecOpsMissionTargetsFragment(): OrderComponent() {

    private var selectedMissionTargetId: Long? = null
    private lateinit var missionTargetBtnsList: LinearLayout
    private val missionTargetIdsToBtns = mutableMapOf<Long, Button>()

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
        val root = inflater.inflate(R.layout.fragment_order_component_specops_mission_types, container, false)

        missionTargetBtnsList = root.findViewById(R.id.missiontargets_btns)

        return root
    }

    private fun setSelectedMissionTarget(missionTargetId: Long?) {
        selectedMissionTargetId = missionTargetId
        updateBtns()
        notifyParentOfSelection()
    }

    private fun updateBtns() {
        missionTargetIdsToBtns.values.forEach { it.setBackgroundColor(getColor(requireContext(), R.color.purple_200)) }
        if(selectedMissionTargetId!=null) {
            val missionTargetBtn = missionTargetIdsToBtns.get(selectedMissionTargetId)
            missionTargetBtn?.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
        }
    }

    override fun getSelectedValue(): Pair<String, String>? {
        if(selectedMissionTargetId!=null) {
            return Pair(OrderDlgArgumentKeys.MissionType.value, selectedMissionTargetId.toString())
        } else {
            return null
        }
    }

    override fun setAllOrderParameters(orderParameters: Map<String, String>) {
        // Do nothing
    }
}