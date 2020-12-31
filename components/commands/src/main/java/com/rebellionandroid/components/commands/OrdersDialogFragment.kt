package com.rebellionandroid.components.commands

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.rebellionandroid.components.commands.enums.OrderDlgArgumentKeys
import com.rebellionandroid.components.commands.enums.OrderDlgComponentTypes
import com.rebellionandroid.components.commands.enums.OrderProcedures
import com.rebellionandroid.components.commands.orderComponents.OrderComponent
import com.rebellionandroid.components.commands.orderComponents.OrderComponentFactoryCtorYardBuildTypesFragment
import com.rebellionandroid.components.commands.orderComponents.OrderComponentFactoryShipYardBuildTypesFragment
import com.rebellionandroid.components.commands.orderComponents.OrderComponentPlanetPickerFragment
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.Utilities


class OrdersDialogFragment: DialogFragment() {

    private lateinit var componentsListLayout: LinearLayout
    private val orderComponents = mutableListOf<OrderComponent>()
    private var currentGameStateId: Long? = null

    companion object {
        fun newInstance(): OrdersDialogFragment {
            return OrdersDialogFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_orders, container, false)

        val titleTextView = root.findViewById<TextView>(R.id.dlg_orders_title_text)
        val positiveBtn = root.findViewById<Button>(R.id.dlg_orders_positive_btn)
        val negativeBtn = root.findViewById<Button>(R.id.dlg_orders_negative_btn)

        when(arguments?.get(OrderDlgArgumentKeys.OrderProcedure.value) as OrderProcedures) {
            OrderProcedures.MoveShip -> {
                titleTextView.text = "Move ship to new planet"
                positiveBtn.text = "Move"
            }
        }

        positiveBtn.setOnClickListener {
            val gameStateViewModel = (activity as BaseActivity).gameStateViewModel
            if(currentGameStateId!=null && arguments!=null) {
                val orderParameters = CommandUtilities.orderComponentsToMap(orderComponents)
                CommandUtilities.conductOrderProcedures(
                    gameStateViewModel,
                    requireArguments(),
                    orderParameters,
                    currentGameStateId!!
                )
            }
            dismiss()
        }
        negativeBtn.setOnClickListener {
            dismiss()
        }

        componentsListLayout = root.findViewById(R.id.dlg_orders_components_list)
        loadComponents()

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
                OrderDlgComponentTypes.PlanetPicker.value -> loadComponent(
                    OrderComponentPlanetPickerFragment.newInstance(),
                    "OrderComponentPlanetPickerFragment"
                )
                else -> OrderComponentFactoryCtorYardBuildTypesFragment.newInstance()
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
        val orderParameters = CommandUtilities.orderComponentsToMap(orderComponents)
        orderComponents.forEach {it.setAllOrderParameters(orderParameters)}
    }

}
