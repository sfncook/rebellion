package com.rebellionandroid.components.commands

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ExpandableListView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.rebellionandroid.components.commands.enums.OrderDlgArgumentKeys
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.database.gamestate.GameStateWithSectors
import com.rebllelionandroid.core.database.gamestate.ShipWithUnits
import kotlinx.coroutines.launch


class ShipMoveDialogFragment: DialogFragment() {

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
        return root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

}
