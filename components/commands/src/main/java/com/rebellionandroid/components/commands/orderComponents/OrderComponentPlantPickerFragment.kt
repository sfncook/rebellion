package com.rebellionandroid.components.commands.orderComponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rebellionandroid.components.commands.R

class OrderComponentPlantPickerFragment(): OrderComponent() {

    companion object {
        fun newInstance(): OrderComponentPlantPickerFragment {
            return OrderComponentPlantPickerFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_order_component_factory_ctoryard_build_types, container, false)

        return root
    }

    override fun getSelectedValue(): Pair<String, String> {
        return Pair("foo", "bar")
    }
}