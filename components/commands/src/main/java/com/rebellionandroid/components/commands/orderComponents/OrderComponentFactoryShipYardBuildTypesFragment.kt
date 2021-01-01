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

class OrderComponentFactoryShipYardBuildTypesFragment(): OrderComponent() {

    private var selectedBuildType: FactoryBuildTargetType? = null
    private lateinit var buildBtnBireme: Button
    private lateinit var buildBtnTrireme: Button
    private lateinit var buildBtnQuadrireme: Button
    private lateinit var buildBtnQuinquereme: Button
    private lateinit var buildBtnHexareme: Button
    private lateinit var buildBtnSeptireme: Button
    private lateinit var buildBtnOctere: Button

    companion object {
        fun newInstance(): OrderComponentFactoryShipYardBuildTypesFragment {
            return OrderComponentFactoryShipYardBuildTypesFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_order_component_factory_shipyard_build_types, container, false)

        buildBtnBireme = root.findViewById(R.id.shipyard_build_2)
        buildBtnTrireme = root.findViewById(R.id.shipyard_build_3)
        buildBtnQuadrireme = root.findViewById(R.id.shipyard_build_4)
        buildBtnQuinquereme = root.findViewById(R.id.shipyard_build_5)
        buildBtnHexareme = root.findViewById(R.id.shipyard_build_6)
        buildBtnSeptireme = root.findViewById(R.id.shipyard_build_7)
        buildBtnOctere = root.findViewById(R.id.shipyard_build_8)

        buildBtnBireme.setOnClickListener { setSelectedBuildType(FactoryBuildTargetType.ShipYard_Bireme) }
        buildBtnTrireme.setOnClickListener { setSelectedBuildType(FactoryBuildTargetType.ShipYard_Trireme) }
        buildBtnQuadrireme.setOnClickListener { setSelectedBuildType(FactoryBuildTargetType.ShipYard_Quadrireme) }
        buildBtnQuinquereme.setOnClickListener { setSelectedBuildType(FactoryBuildTargetType.ShipYard_Quinquereme) }
        buildBtnHexareme.setOnClickListener { setSelectedBuildType(FactoryBuildTargetType.ShipYard_Hexareme) }
        buildBtnSeptireme.setOnClickListener { setSelectedBuildType(FactoryBuildTargetType.ShipYard_Septireme) }
        buildBtnOctere.setOnClickListener { setSelectedBuildType(FactoryBuildTargetType.ShipYard_Octere) }

        updateBtns()

        return root
    }

    private fun setSelectedBuildType(buildTargetType: FactoryBuildTargetType?) {
        selectedBuildType = buildTargetType
        updateBtns()
        notifyParentOfSelection()
    }

    private fun updateBtns() {
        buildBtnBireme.setBackgroundColor(getColor(requireContext(), R.color.purple_200))
        buildBtnTrireme.setBackgroundColor(getColor(requireContext(), R.color.purple_200))
        buildBtnQuadrireme.setBackgroundColor(getColor(requireContext(), R.color.purple_200))
        buildBtnQuinquereme.setBackgroundColor(getColor(requireContext(), R.color.purple_200))
        buildBtnHexareme.setBackgroundColor(getColor(requireContext(), R.color.purple_200))
        buildBtnSeptireme.setBackgroundColor(getColor(requireContext(), R.color.purple_200))
        buildBtnOctere.setBackgroundColor(getColor(requireContext(), R.color.purple_200))
        when(selectedBuildType) {
            FactoryBuildTargetType.ShipYard_Bireme -> buildBtnBireme.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
            FactoryBuildTargetType.ShipYard_Trireme -> buildBtnTrireme.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
            FactoryBuildTargetType.ShipYard_Quadrireme -> buildBtnQuadrireme.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
            FactoryBuildTargetType.ShipYard_Quinquereme -> buildBtnQuinquereme.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
            FactoryBuildTargetType.ShipYard_Hexareme -> buildBtnHexareme.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
            FactoryBuildTargetType.ShipYard_Septireme -> buildBtnSeptireme.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
            FactoryBuildTargetType.ShipYard_Octere -> buildBtnOctere.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
            else -> println("")
        }
    }

    override fun getSelectedValue(): Map<String, String?> {
        return mapOf(OrderDlgArgumentKeys.BuildTargetType.value to selectedBuildType?.value)
    }

    override fun setAllOrderParameters(orderParameters: Map<String, String?>) {
        // Do nothing
    }
}