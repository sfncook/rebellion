package com.rebellionandroid.components.commands

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.database.gamestate.GameStateWithSectors


class ShipMoveDialogFragment: DialogFragment() {

    private lateinit var rootContext: Context
    private lateinit var sectorsAndPlanetsExpandableList: ExpandableListView
//    var expandableListAdapter: ExpandableListAdapter? = null
//    var expandableListTitle: List<String>? = null
//    var expandableListDetail: HashMap<String, List<String>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_ship_move, container, false)
        rootContext = root.context

        root.findViewById<MaterialButton>(R.id.ship_move_close_btn).setOnClickListener {
            dismiss()
        }

        sectorsAndPlanetsExpandableList = root.findViewById(R.id.ship_move_list_sector_and_planets) as ExpandableListView

        val gameStateViewModel = (activity as BaseActivity).gameStateViewModel
        gameStateViewModel.gameState.observe(viewLifecycleOwner , {
            updateSectorsList(it)
        })

//        expandableListDetail = data
//        expandableListTitle = ArrayList(expandableListDetail!!.keys)
//        expandableListAdapter = CustomExpandableListAdapter(
//            root.context,
//            expandableListTitle!!,
//            expandableListDetail!!
//        )
//        expandableListView!!.setAdapter(expandableListAdapter)
//        expandableListView!!.setOnGroupExpandListener { groupPosition ->
//            println((expandableListTitle as ArrayList<String>).get(groupPosition) + " List Expanded.")
//        }
//
//        expandableListView!!.setOnGroupCollapseListener { groupPosition ->
//            Toast.makeText(
//                root.context,
//                (expandableListTitle as ArrayList<String>).get(groupPosition) + " List Collapsed.",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//
//        expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
//            Toast.makeText(
//                root.context,
//                (expandableListTitle as ArrayList<String>).get(groupPosition) + " -> "
//                        + expandableListDetail!![(expandableListTitle as ArrayList<String>).get(groupPosition)]!![childPosition],
//                Toast.LENGTH_SHORT
//            ).show()
//            false
//        }

        return root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun updateSectorsList(gameStateWithSectors: GameStateWithSectors) {
        val sectors = gameStateWithSectors.sectors
        val sortedSectors = sectors.toSortedSet(Comparator { s1, s2 ->
            s1.sector.name.compareTo(s2.sector.name)
        })
        val sectorsAndPlanetsListAdapter = SectorsAndPlanetsListAdapter(rootContext, sortedSectors.toList())
        sectorsAndPlanetsExpandableList.setAdapter(sectorsAndPlanetsListAdapter)
        sectorsAndPlanetsExpandableList.setOnGroupClickListener { parent, v, groupPosition, childPosition ->
            println("click")
            false
        }
    }
}
