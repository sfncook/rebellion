package com.rebellionandroid.components.commands

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.rebellionandroid.components.commands.ExpandableListDataPump.data


class ShipMoveDialogFragment: DialogFragment() {

    var expandableListView: ExpandableListView? = null
    var expandableListAdapter: ExpandableListAdapter? = null
    var expandableListTitle: List<String>? = null
    var expandableListDetail: HashMap<String, List<String>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_ship_move, container, false)

        root.findViewById<MaterialButton>(R.id.unit_cmd_close_btn).setOnClickListener {
            dismiss()
        }

        expandableListView = root.findViewById(R.id.expandableListView) as ExpandableListView
        expandableListDetail = data
        expandableListTitle = ArrayList(expandableListDetail!!.keys)
        expandableListAdapter = CustomExpandableListAdapter(
            root.context, expandableListTitle!!,
            expandableListDetail!!
        )
        expandableListView!!.setAdapter(expandableListAdapter)
        expandableListView!!.setOnGroupExpandListener { groupPosition ->
            println((expandableListTitle as ArrayList<String>).get(groupPosition) + " List Expanded.")
        }

        expandableListView!!.setOnGroupCollapseListener { groupPosition ->
            Toast.makeText(
                root.context,
                (expandableListTitle as ArrayList<String>).get(groupPosition) + " List Collapsed.",
                Toast.LENGTH_SHORT
            ).show()
        }

        expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            Toast.makeText(
                root.context,
                (expandableListTitle as ArrayList<String>).get(groupPosition) + " -> "
                        + expandableListDetail!![(expandableListTitle as ArrayList<String>).get(groupPosition)]!![childPosition],
                Toast.LENGTH_SHORT
            ).show()
            false
        }

        return root
    }

    override fun onStart() {
        super.onStart()
        dialog?.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}