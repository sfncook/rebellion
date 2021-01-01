package com.rebellionandroid.components.commands.orderComponents

import androidx.fragment.app.Fragment
import com.rebellionandroid.components.commands.OrdersDialogFragment

abstract class OrderComponent(): Fragment() {
    abstract fun getSelectedValue(): Map<String, String?>
    abstract fun setAllOrderParameters(orderParameters: Map<String, String?>)

    fun notifyParentOfSelection() {
        if(parentFragment!=null && parentFragment!!::class == OrdersDialogFragment::class) {
            (parentFragment as OrdersDialogFragment).onComponentSelection()
        }
    }
}