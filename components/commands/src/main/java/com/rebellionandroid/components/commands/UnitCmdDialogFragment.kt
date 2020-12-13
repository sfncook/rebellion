package com.rebellionandroid.components.commands

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.rebllelionandroid.core.BaseActivity


class UnitCmdDialogFragment: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_unit_cmd, container, false)
        val gameStateViewModel = (activity as BaseActivity).gameStateViewModel

        root.findViewById<MaterialButton>(R.id.unit_cmd_close_btn).setOnClickListener {
            dismiss()
        }

        root.findViewById<Button>(R.id.unit_cmd_btn_sabotage).setOnClickListener{

        }

        gameStateViewModel.getAllPlanets() { planets ->
//            listPlanets.adapter = PlanetListAdapter(planets)
        }

        return root
    }
}