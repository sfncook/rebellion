package com.rebellionandroid.components.commands.orderComponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.getColor
import com.rebellionandroid.components.commands.R
import com.rebellionandroid.components.commands.enums.OrderDlgArgumentKeys
import com.rebllelionandroid.core.database.gamestate.enums.FactoryBuildTargetType

class OrderComponentFactoryTrainingFacBuildTypesFragment(): OrderComponent() {

    private var selectedBuildType: FactoryBuildTargetType? = null
    private lateinit var buildBtnSpecOps: Button
    private lateinit var buildBtnGarrison: Button

    companion object {
        fun newInstance(): OrderComponentFactoryTrainingFacBuildTypesFragment {
            return OrderComponentFactoryTrainingFacBuildTypesFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_order_component_factory_trainingfac_build_types, container, false)

        buildBtnSpecOps = root.findViewById(R.id.trainingfac_build_specops)
        buildBtnGarrison = root.findViewById(R.id.trainingfac_build_garrison)

        buildBtnSpecOps.setOnClickListener { setSelectedBuildType(FactoryBuildTargetType.TrainingFac_SpecOps) }
        buildBtnGarrison.setOnClickListener { setSelectedBuildType(FactoryBuildTargetType.TrainingFac_Garrison) }

        updateBtns()

        return root
    }

    private fun setSelectedBuildType(buildTargetType: FactoryBuildTargetType?) {
        selectedBuildType = buildTargetType
        updateBtns()
        notifyParentOfSelection()
    }

    private fun updateBtns() {
        buildBtnSpecOps.setBackgroundColor(getColor(requireContext(), R.color.purple_200))
        buildBtnGarrison.setBackgroundColor(getColor(requireContext(), R.color.purple_200))
        when(selectedBuildType) {
            FactoryBuildTargetType.TrainingFac_SpecOps -> buildBtnSpecOps.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
            FactoryBuildTargetType.TrainingFac_Garrison -> buildBtnGarrison.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
            else -> println("")
        }
    }

    override fun getSelectedValue(): Pair<String, String>? {
        if(selectedBuildType!=null) {
            return Pair(OrderDlgArgumentKeys.BuildTargetType.value, selectedBuildType!!.value)
        } else {
            return null
        }
    }

    override fun setAllOrderParameters(orderParameters: Map<String, String>) {
        // Do nothing
    }
}