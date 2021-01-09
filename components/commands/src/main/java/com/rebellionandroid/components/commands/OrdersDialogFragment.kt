package com.rebellionandroid.components.commands

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.rebellionandroid.components.commands.enums.OrderDlgArgumentKeys
import com.rebellionandroid.components.commands.enums.OrderDlgComponentTypes
import com.rebellionandroid.components.commands.enums.OrderProcedures
import com.rebellionandroid.components.commands.orderComponents.*
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.commands.CommandUtilities
import com.rebllelionandroid.core.database.gamestate.enums.FactoryBuildTargetType
import com.rebllelionandroid.core.database.gamestate.enums.MissionTargetType
import com.rebllelionandroid.core.database.gamestate.enums.MissionType


class OrdersDialogFragment: DialogFragment() {

    private lateinit var componentsListLayout: LinearLayout
    private val orderComponents = mutableListOf<OrderComponent>()
    private var currentGameStateId: Long? = null
    private lateinit var positiveBtn: Button

    companion object {
        fun newInstance(): OrdersDialogFragment {
            return OrdersDialogFragment()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_orders, container, false)

        val titleTextView = root.findViewById<TextView>(R.id.dlg_orders_title_text)
        positiveBtn = root.findViewById<Button>(R.id.dlg_orders_positive_btn)
        val negativeBtn = root.findViewById<Button>(R.id.dlg_orders_negative_btn)

        when(arguments?.get(OrderDlgArgumentKeys.OrderProcedure.value) as OrderProcedures) {
            OrderProcedures.MoveShip -> {
                titleTextView.text = "Move ship to new planet"
                positiveBtn.text = "Move"
            }
            OrderProcedures.FactoryBuild -> {
                titleTextView.text = "Build order"
                positiveBtn.text = "Build"
            }
            OrderProcedures.AssignMission -> {
                titleTextView.text = "Assign mission"
                positiveBtn.text = "Assign"
            }
        }

        positiveBtn.setOnClickListener {
            if(allComponentsSelected()) {
                val gameStateViewModel = (activity as BaseActivity).gameStateViewModel
                if(currentGameStateId!=null && arguments!=null) {
                    val orderParameters = orderComponentsToMap(orderComponents)
                    conductOrderProcedures(
                        gameStateViewModel,
                        requireArguments(),
                        orderParameters,
                        currentGameStateId!!
                    )
                }
                dismiss()
            }
        }
        negativeBtn.setOnClickListener {
            dismiss()
        }

        componentsListLayout = root.findViewById(R.id.dlg_orders_components_list)
        loadComponents()

        updatePositiveBtn()

        return root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onResume() {
        super.onResume()
        currentGameStateId = Utilities.getCurrentGameStateId(
            getString(R.string.gameStateSharedPrefFile),
            getString(R.string.keyCurrentGameId),
            requireActivity()
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadComponents() {
        val componentsToShow = arguments?.getStringArrayList(OrderDlgArgumentKeys.ComponentsToShow.value)!!
        componentsToShow.map { componentToShow ->
            when(componentToShow) {
                OrderDlgComponentTypes.CtorYardBuildTypes.value -> loadComponent(
                    OrderComponentFactoryCtorYardBuildTypesFragment.newInstance(),
                    "FactoryCtorYardBuildOrderFragment"
                )
                OrderDlgComponentTypes.ShipYardBuildTypes.value -> loadComponent(
                    OrderComponentFactoryShipYardBuildTypesFragment.newInstance(),
                    "OrderComponentFactoryShipYardBuildTypesFragment"
                )
                OrderDlgComponentTypes.TrainingFacBuildTypes.value -> loadComponent(
                    OrderComponentFactoryTrainingFacBuildTypesFragment.newInstance(),
                    "OrderComponentFactoryTrainingFacBuildTypesFragment"
                )
                OrderDlgComponentTypes.PlanetPicker.value -> loadComponent(
                    OrderComponentPlanetPickerFragment.newInstance(),
                    "OrderComponentPlanetPickerFragment"
                )
                OrderDlgComponentTypes.SpecOpsMissionTypes.value -> loadComponent(
                    OrderComponentSpecOpsMissionTypesFragment.newInstance(arguments),
                    "OrderComponentSpecOpsMissionTypesFragment"
                )
                OrderDlgComponentTypes.SpecOpsMissionTargets.value -> loadComponent(
                    OrderComponentSpecOpsMissionTargetsFragment.newInstance(arguments),
                    "OrderComponentSpecOpsMissionTargetsFragment"
                )
            }
        }
    }

    private fun loadComponent(orderComponent: OrderComponent, tag: String) {
        orderComponents.add(orderComponent)
        childFragmentManager.beginTransaction().add(
            R.id.dlg_orders_components_list,
            orderComponent,
            tag
        ).commit()
    }

    fun onComponentSelection() {
        val orderParameters = orderComponentsToMap(orderComponents)
        orderComponents.forEach {it.setAllOrderParameters(orderParameters)}
        updatePositiveBtn()
    }

    private fun updatePositiveBtn() {
        positiveBtn.isEnabled = allComponentsSelected()
    }

    private fun allComponentsSelected(): Boolean {
        return orderComponents.all { orderComponent ->
            orderComponent.getSelectedValue().values.all { it!=null }
        }
    }

    private fun conductOrderProcedures(
        gameStateViewModel: GameStateViewModel,
        bundle: Bundle,
        orderParameters: Map<String, String?>,
        currentGameStateId: Long
    ) {
        when(bundle.get(OrderDlgArgumentKeys.OrderProcedure.value) as OrderProcedures) {
            OrderProcedures.MoveShip -> {
                val shipId = bundle.getLong(OrderDlgArgumentKeys.MoveShipId.value)
                val destPlanetId = orderParameters[OrderDlgArgumentKeys.SelectedPlanetId.value]
                if (destPlanetId != null) {
                    CommandUtilities.moveShipToPlanet(
                        gameStateViewModel,
                        shipId,
                        destPlanetId.toLong(),
                        currentGameStateId
                    )
                } else {
                    println("ERROR: MoveShip missing parameters")
                }
            }

            OrderProcedures.FactoryBuild -> {
                val factoryId = bundle.getLong(OrderDlgArgumentKeys.FactoryId.value)
                val destPlanetId = orderParameters[OrderDlgArgumentKeys.SelectedPlanetId.value]
                val buildTargetTypeStr = orderParameters[OrderDlgArgumentKeys.BuildTargetType.value]
                if(buildTargetTypeStr != null) {
                    val buildTargetType = FactoryBuildTargetType.valueOf(buildTargetTypeStr)
                    if (destPlanetId != null) {
                        CommandUtilities.factoryBuild(
                            gameStateViewModel,
                            factoryId,
                            destPlanetId.toLong(),
                            buildTargetType,
                            currentGameStateId
                        )
                    } else {
                        println("ERROR: MoveShip missing parameters")
                    }
                }
            }

            OrderProcedures.AssignMission -> {
                val personnelId = bundle.getLong(OrderDlgArgumentKeys.PersonnelId.value)
                val missionTypeStr = orderParameters[OrderDlgArgumentKeys.MissionType.value]
                val missionTargetTypeStr = orderParameters[OrderDlgArgumentKeys.MissionTargetType.value]
                val missionTargetId = orderParameters[OrderDlgArgumentKeys.MissionTargetId.value]
                if(missionTypeStr!=null && missionTargetTypeStr!=null && missionTargetId!=null) {
                    val missionType = MissionType.valueOf(missionTypeStr)
                    val missionTargetType = MissionTargetType.valueOf(missionTargetTypeStr)
                    CommandUtilities.assignMission(
                        gameStateViewModel,
                        personnelId,
                        currentGameStateId,
                        missionType,
                        missionTargetType,
                        missionTargetId.toLong()
                    )
                } else {
                    println("ERROR: assign mission missing parameters")
                }
            }

            else -> {}
        }// when orderProcedure
    }//conductOrderProcedures

    private fun orderComponentsToMap(orderComponents: List<OrderComponent>): Map<String, String?> {
        val orderParameters = mutableMapOf<String, String?>()
        orderComponents.forEach { orderComponent ->
            orderParameters.putAll(orderComponent.getSelectedValue())
        }
        return orderParameters
    }

}
