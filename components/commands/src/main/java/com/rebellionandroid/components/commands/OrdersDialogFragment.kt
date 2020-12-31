package com.rebellionandroid.components.commands

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.rebellionandroid.components.commands.enums.OrderDlgArgumentKeys
import com.rebellionandroid.components.commands.enums.OrderDlgComponentTypes
import com.rebellionandroid.components.commands.orderComponents.OrderComponent
import com.rebellionandroid.components.commands.orderComponents.OrderComponentFactoryCtorYardBuildTypesFragment
import com.rebellionandroid.components.commands.orderComponents.OrderComponentPlanetPickerFragment


class OrdersDialogFragment: DialogFragment() {

    private lateinit var componentsListLayout: LinearLayout
    private val componentFragments = mutableListOf<OrderComponent>()

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

        val positiveBtnText = arguments?.getString(OrderDlgArgumentKeys.PositiveBtnText.value)!!
        val negativeBtnText = arguments?.getString(OrderDlgArgumentKeys.NegativeBtnText.value)!!

        val positiveBtn = root.findViewById<Button>(R.id.dlg_orders_positive_btn)
        val negativeBtn = root.findViewById<Button>(R.id.dlg_orders_negative_btn)

        positiveBtn.text = positiveBtnText
        negativeBtn.text = negativeBtnText

        positiveBtn.setOnClickListener {
            val intent = Intent()
            intent.putExtra("FOO", "bar1234")
            targetFragment?.onActivityResult(getTargetRequestCode(), 1234, intent)
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

    private fun loadComponents() {
        val componentsToShow = arguments?.getStringArrayList(OrderDlgArgumentKeys.ComponentsToShow.value)!!
        componentsToShow.map { componentToShow ->
            when(componentToShow) {
                OrderDlgComponentTypes.CtorYardBuildTypes.value -> loadComponent(
                    OrderComponentFactoryCtorYardBuildTypesFragment.newInstance(),
                    "FactoryCtorYardBuildOrderFragment"
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
        componentFragments.add(orderComponent)
        childFragmentManager.beginTransaction().add(
            R.id.dlg_orders_components_list,
            orderComponent,
            tag
        ).commit()
    }

}
