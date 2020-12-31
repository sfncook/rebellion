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

class OrderComponentFactoryCtorYardBuildTypesFragment(): OrderComponent() {

    private var selectedBuildType: FactoryBuildTargetType? = null
    private lateinit var buildBtnCtorYard: Button
    private lateinit var buildBtnShipYard: Button
    private lateinit var buildBtnTraining: Button
    private lateinit var buildBtnPlanetShield: Button
    private lateinit var buildBtnOrbitBattery: Button

    companion object {
        fun newInstance(): OrderComponentFactoryCtorYardBuildTypesFragment {
            return OrderComponentFactoryCtorYardBuildTypesFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_order_component_factory_ctoryard_build_types, container, false)

        buildBtnCtorYard = root.findViewById(R.id.factmove_build_constructionyard)
        buildBtnShipYard = root.findViewById(R.id.factmove_build_shipyard)
        buildBtnTraining = root.findViewById(R.id.factmove_build_trainingfacility)
        buildBtnPlanetShield = root.findViewById(R.id.factmove_build_planetaryshield)
        buildBtnOrbitBattery = root.findViewById(R.id.factmove_build_orbitalbattery)

        buildBtnCtorYard.setOnClickListener { setSelectedBuildType(FactoryBuildTargetType.ConstructionYard_ConstructionYard) }
        buildBtnShipYard.setOnClickListener { setSelectedBuildType(FactoryBuildTargetType.ConstructionYard_ShipYard) }
        buildBtnTraining.setOnClickListener { setSelectedBuildType(FactoryBuildTargetType.ConstructionYard_TrainingFacility) }
        buildBtnPlanetShield.setOnClickListener { setSelectedBuildType(FactoryBuildTargetType.ConstructionYard_PlanetaryShield) }
        buildBtnOrbitBattery.setOnClickListener { setSelectedBuildType(FactoryBuildTargetType.ConstructionYard_OrbitalBattery) }

        updateBtns()

        return root
    }

    private fun setSelectedBuildType(buildTargetType: FactoryBuildTargetType?) {
        selectedBuildType = buildTargetType
        updateBtns()
    }

    private fun updateBtns() {
        buildBtnCtorYard.setBackgroundColor(getColor(requireContext(), R.color.purple_200))
        buildBtnShipYard.setBackgroundColor(getColor(requireContext(), R.color.purple_200))
        buildBtnTraining.setBackgroundColor(getColor(requireContext(), R.color.purple_200))
        buildBtnPlanetShield.setBackgroundColor(getColor(requireContext(), R.color.purple_200))
        buildBtnOrbitBattery.setBackgroundColor(getColor(requireContext(), R.color.purple_200))
        when(selectedBuildType) {
            FactoryBuildTargetType.ConstructionYard_ConstructionYard -> buildBtnCtorYard.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
            FactoryBuildTargetType.ConstructionYard_ShipYard -> buildBtnShipYard.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
            FactoryBuildTargetType.ConstructionYard_TrainingFacility -> buildBtnTraining.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
            FactoryBuildTargetType.ConstructionYard_PlanetaryShield -> buildBtnPlanetShield.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
            FactoryBuildTargetType.ConstructionYard_OrbitalBattery -> buildBtnOrbitBattery.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
            else -> println("")
        }
    }

    override fun getSelectedValue(): Pair<String, String> {
        if(selectedBuildType!=null) {
            return Pair(OrderDlgArgumentKeys.BuildTargetType.value, selectedBuildType!!.value)
        } else {
            return Pair("","")
        }
    }
}