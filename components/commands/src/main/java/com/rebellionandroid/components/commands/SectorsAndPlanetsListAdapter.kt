package com.rebellionandroid.components.commands

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.gamestate.SectorWithPlanets
import com.rebllelionandroid.core.database.gamestate.ShipWithUnits
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty


internal class SectorsAndPlanetsListAdapter(
    private val context: Context,
    private val sectorsWithPlanets: List<SectorWithPlanets>,
    private val selectedShipWithUnits: ShipWithUnits
) : BaseExpandableListAdapter() {

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        val sectorWithPlanets = getGroup(listPosition) as SectorWithPlanets
        val planetsWithUnits = sectorWithPlanets.planets
        val sortedPlanets = Utilities.sortPlanets(planetsWithUnits)
        return sortedPlanets[expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        val planetWithUnits = getChild(listPosition, expandedListPosition) as PlanetWithUnits
        return planetWithUnits.planet.id
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
        val expandedListTextView = convertView2?.findViewById<TextView>(R.id.ship_move_planet_name)
        if (expandedListTextView != null) {
            expandedListTextView.text = planetWithUnits.planet.name
        }

        val (imgId, colorId) = Utilities.getLoyaltyIconForPlanet(planetWithUnits.planet)
        val loyaltyImg = convertView2?.findViewById<ImageView>(R.id.ship_move_planet_loyalty_img)
        if(loyaltyImg != null) {
            loyaltyImg.setImageResource(imgId)
            loyaltyImg.setColorFilter(
                ContextCompat.getColor(context, colorId),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
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
            convertView2 = layoutInflater.inflate(R.layout.list_item_sector_2, parent, false)
        }
        val sectorNameTextView = convertView2?.findViewById(R.id.sector_name) as TextView
        val manyShipsInSectorTxt: TextView = convertView2.findViewById(com.rebellionandroid.components.entityUi.R.id.many_ships_in_sector_txt)
        val shipsInSectorImg: ImageView = convertView2.findViewById(com.rebellionandroid.components.entityUi.R.id.ships_in_sector_img)
        val manyEnemyShipsInSectorTxt: TextView = convertView2.findViewById(com.rebellionandroid.components.entityUi.R.id.many_enemy_ships_in_sector_txt)
        val enemyShipsInSectorImg: ImageView = convertView2.findViewById(com.rebellionandroid.components.entityUi.R.id.enemy_ships_in_sector_img)

        sectorNameTextView.setTypeface(null, Typeface.BOLD)
        sectorNameTextView.text = sectorWithPlanets.sector.name

        val planetsList = convertView2?.findViewById<LinearLayout>(R.id.planets_list)
        planetsList.removeAllViews()
        val planetsWithUnits = sectorWithPlanets.planets
        Utilities.sortPlanets(planetsWithUnits).forEach { planetWithUnits ->
            val planet = planetWithUnits.planet
            val (imgId, colorId) = Utilities.getLoyaltyIconForPlanet(planet)
            val imgView = ImageView(context)
            imgView.layoutParams = LinearLayout.LayoutParams(30, 30)
            imgView.setImageResource(imgId)
            imgView.setColorFilter(
                ContextCompat.getColor(context, colorId),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
            planetsList.addView(imgView)
        }

        Utilities.populateShipsInSectorUi(
            sectorWithPlanets,
            manyShipsInSectorTxt,
            shipsInSectorImg,
            manyEnemyShipsInSectorTxt,
            enemyShipsInSectorImg
        )

        return convertView2
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }

}
