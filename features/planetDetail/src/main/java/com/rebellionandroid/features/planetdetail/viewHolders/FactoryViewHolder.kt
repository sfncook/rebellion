package com.rebellionandroid.features.planetdetail.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.rebellionandroid.components.commands.FactoryBuildDialogFragment
import com.rebellionandroid.features.planetdetail.R
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.Factory
import com.rebllelionandroid.core.database.gamestate.Planet
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.gamestate.enums.FactoryType
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty

class FactoryViewHolder(
    view: View,
    structureList: List<Any>,
    planetWithUnits: PlanetWithUnits,
    myTeam: TeamLoyalty
) : RecyclerView.ViewHolder(view) {
    val factoryLabel: TextView = view.findViewById(R.id.factory_label)
    val factoryImg: ImageView = view.findViewById(R.id.factory_img)
    val buildInfoContainer: View = view.findViewById(R.id.factory_buildinfo_container)
    val buildTargetTypeText: TextView = view.findViewById(R.id.factory_buildTargetType)
    val buildTargetDueDateText: TextView = view.findViewById(R.id.factory_buildTarget_duedate)
    val buildTargetDeliverText: TextView = view.findViewById(R.id.factory_buildTarget_deliver)

    init {
        val planetTeam = Utilities.getPlanetLoyalty(planetWithUnits.planet)
        if(myTeam == planetTeam) {
            view.setOnClickListener {
                val structureObj = structureList[adapterPosition]
                if (structureObj::class == Factory::class) {
                    val factory = structureObj as Factory
                    val bundle = bundleOf(
                        "factoryId" to factory.id
                    )
                    val fm: FragmentManager =
                        (it.context as BaseActivity).supportFragmentManager
                    val factoryBuildDialogFragment = FactoryBuildDialogFragment()
                    factoryBuildDialogFragment.arguments = bundle
                    factoryBuildDialogFragment.show(fm, "shipMoveDialogFragment")
                } else {
                    println("Whoops! the FactoryViewHolder got a click event for an object that is not of type Factory: ${structureObj::class.qualifiedName}")
                }
            }
        }// if
    }// init

    fun bindViewHolder(factory: Factory, deliverToPlanet: Planet?) {
        factoryLabel.text = factory.factoryType.value
        val imgSrc  = when(factory.factoryType) {
            FactoryType.ConstructionYard -> R.drawable.factory_ctor_yard
            FactoryType.ShipYard -> R.drawable.factory_ship_yard
            FactoryType.TrainingFaciliy -> R.drawable.factory_training_facility
        }
        factoryImg.setImageResource(imgSrc)
        if(factory.buildTargetType!=null) {
            buildInfoContainer.visibility = View.VISIBLE
            buildTargetDueDateText.text = factory.dayBuildComplete.toString()
            buildTargetTypeText.text = factory.buildTargetType?.value
            if (deliverToPlanet != null) {
                buildTargetDeliverText.text = deliverToPlanet.name
            }
        } else {
            buildInfoContainer.visibility = View.GONE
        }
    }
}