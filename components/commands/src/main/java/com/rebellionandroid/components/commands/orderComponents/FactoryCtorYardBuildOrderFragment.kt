package com.rebellionandroid.components.commands.orderComponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rebellionandroid.components.commands.R

class FactoryCtorYardBuildOrderFragment(): Fragment(), OrderComponent {

    companion object {
        fun newInstance(): FactoryCtorYardBuildOrderFragment {
            return FactoryCtorYardBuildOrderFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_factory_ctoryard_build_order, container, false)

        return root
    }

    override fun getSelectedValue(): Pair<String, String> {
        return Pair("foo", "bar")
    }
}