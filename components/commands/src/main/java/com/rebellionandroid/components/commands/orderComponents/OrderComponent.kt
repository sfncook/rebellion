package com.rebellionandroid.components.commands.orderComponents

import androidx.fragment.app.Fragment

abstract class OrderComponent(): Fragment() {
    abstract fun getSelectedValue(): Pair<String, String>
}