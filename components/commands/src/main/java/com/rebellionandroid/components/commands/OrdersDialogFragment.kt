package com.rebellionandroid.components.commands

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.rebellionandroid.components.commands.enums.OrderDlgArgumentKeys
import com.rebellionandroid.components.commands.enums.OrderDlgComponentTypes
import com.rebellionandroid.components.commands.orderComponents.FactoryCtorYardBuildOrderFragment


class OrdersDialogFragment: DialogFragment() {

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
            dismiss()
        }
        negativeBtn.setOnClickListener {
            dismiss()
        }


//        val dlg_orders_ctoryard_build_component = root.findViewById<View>(R.id.dlg_orders_ctoryard_build_component)
        val frag = requireActivity().supportFragmentManager.findFragmentByTag("dlg_orders_ctoryard_build_component_tag")
        loadComponents()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val findViewWithTag =
//            view.findViewWithTag<FragmentContainerView>("dlg_orders_ctoryard_build_component_tag")

        val findFragmentById =
            requireActivity().supportFragmentManager.findFragmentById(R.id.dlg_orders_ctoryard_build_component)
        val findFragmentByTag =
            requireActivity().supportFragmentManager.findFragmentByTag("dlg_orders_ctoryard_build_component_tag")

        println("asdf")
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    private fun loadComponents() {
//        val componentsToShow = arguments?.getStringArrayList(OrderDlgArgumentKeys.ComponentsToShow.value)!!
//        val fragmentsToLoad = componentsToShow.map { componentToShow ->
//            when(componentToShow) {
//                OrderDlgComponentTypes.CtorYardBuildTypes.value -> FactoryCtorYardBuildOrderFragment.newInstance()
//                else -> FactoryCtorYardBuildOrderFragment.newInstance()
//            }
//        }

    }

}
