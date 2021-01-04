package com.rebellionandroid.features.planetdetail.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.rebellionandroid.components.entityUi.dialog.DialogModalFragment
import com.rebellionandroid.components.entityUi.dialog.components.DialogComponentParamKeys
import com.rebellionandroid.components.entityUi.dialog.components.DialogComponentTypes
import com.rebellionandroid.features.planetdetail.R
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.DefenseStructure
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.gamestate.enums.DefenseStructureType
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty

class DefenseStructureViewHolder(
    view: View,
    structureList: List<Any>,
    planetWithUnits: PlanetWithUnits,
    myTeam: TeamLoyalty
) : RecyclerView.ViewHolder(view) {
    val label: TextView = view.findViewById(R.id.label)
    val img: ImageView = view.findViewById(R.id.img)

    init {
        val planetTeam = Utilities.getPlanetLoyalty(planetWithUnits.planet)
        if(myTeam == planetTeam) {
            view.setOnClickListener {
                val structureObj = structureList[adapterPosition]
                if (structureObj::class == DefenseStructure::class) {
                    val structure = structureObj as DefenseStructure

                    val componentsToShow = arrayListOf(
                        DialogComponentTypes.RetireStructure.value
                    )
                    val bundle = bundleOf(
                        DialogComponentParamKeys.StructureId.value to structure.id,
                        DialogComponentParamKeys.ComponentsToShow.value to componentsToShow
                    )
                    val fm: FragmentManager = (it.context as BaseActivity).supportFragmentManager
                    val frag = DialogModalFragment.newInstance(titleText = structure.defenseStructureType.value, bundle)
                    frag.show(fm, "DefenseStructure")
                } else {
                    println("Whoops! the DefenseStructureViewHolder got a click event for an object that is not of type DefenseStructure    : ${structureObj::class.qualifiedName}")
                }
            }
        }// if
    }// init

    fun bindViewHolder(structure: DefenseStructure) {
        label.text = structure.defenseStructureType.value
        val imgSrc  = when(structure.defenseStructureType) {
            DefenseStructureType.PlanetaryShield -> R.drawable.planetary_shield
            DefenseStructureType.OrbitalBattery -> R.drawable.orbital_battery
        }
        img.setImageResource(imgSrc)
    }
}
