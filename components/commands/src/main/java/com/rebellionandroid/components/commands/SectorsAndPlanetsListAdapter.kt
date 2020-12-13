package com.rebellionandroid.components.commands

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.gamestate.SectorWithPlanets


internal class SectorsAndPlanetsListAdapter(
    private val context: Context,
    private val sectorsWithPlanets: List<SectorWithPlanets>,
) : BaseExpandableListAdapter() {

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return sectorsWithPlanets[listPosition].planets[expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return sectorsWithPlanets[listPosition].planets[expandedListPosition].planet.id
    }

    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        val planetWithUnits = getChild(listPosition, expandedListPosition) as PlanetWithUnits
        var convertView2: View? = convertView
        if (convertView2 == null) {
            val layoutInflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView2 = layoutInflater.inflate(R.layout.ship_move_list_item_planet, null)
        }
        val expandedListTextView = convertView2?.findViewById<TextView>(R.id.expandedListItem)
        if (expandedListTextView != null) {
            expandedListTextView.text = planetWithUnits.planet.name
        }
        return convertView2
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return sectorsWithPlanets[listPosition].planets.size
    }

    override fun getGroup(listPosition: Int): Any {
        return sectorsWithPlanets[listPosition]
    }

    override fun getGroupCount(): Int {
        return sectorsWithPlanets.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup?
    ): View {
        var convertView2: View? = convertView
        val sectorWithPlanets = getGroup(listPosition) as SectorWithPlanets
        if (convertView2 == null) {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView2 = layoutInflater.inflate(R.layout.ship_move_list_sector_group, parent, false)
        }
        val listTitleTextView = convertView2?.findViewById(R.id.listTitle) as TextView
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = sectorWithPlanets.sector.name
        return convertView2
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }

}