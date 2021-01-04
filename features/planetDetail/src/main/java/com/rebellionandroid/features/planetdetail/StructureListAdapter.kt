package com.rebellionandroid.features.planetdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rebellionandroid.features.planetdetail.viewHolders.DefenseStructureViewHolder
import com.rebellionandroid.features.planetdetail.viewHolders.FactoryViewHolder
import com.rebllelionandroid.core.database.gamestate.DefenseStructure
import com.rebllelionandroid.core.database.gamestate.Factory
import com.rebllelionandroid.core.database.gamestate.Planet
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.gamestate.enums.DefenseStructureType
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty

class StructureListAdapter(
    private val planetWithUnits: PlanetWithUnits,
    private val myTeam: TeamLoyalty,
    private val planetIdsToPlanets: Map<Long, Planet>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val structuresList = mutableListOf<Any>()

    enum class StructureType(val value: Int) {
        Factory(1),
        Defense(2),
        Empty(3),
    }

    private val EMPTY_ENERGY = "EMPTY_ENERGY"

    init {
        structuresList.addAll(planetWithUnits.factories)
        structuresList.addAll(planetWithUnits.defenseStructures)
        val manyEnergyUsed = (planetWithUnits.factories.size + planetWithUnits.defenseStructures.size)
        val manyEnergyEmpty = planetWithUnits.planet.energyCap - manyEnergyUsed
        for (i in 1 .. manyEnergyEmpty) {
            structuresList.add(EMPTY_ENERGY)
        }
    }

    class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemViewType(position: Int): Int {
        val structure = structuresList[position]
        return when(structure::class) {
            Factory::class -> StructureType.Factory.value
            DefenseStructure::class -> StructureType.Defense.value
            else -> StructureType.Empty.value
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutId = when(viewType) {
            StructureType.Factory.value -> R.layout.list_item_factory
            StructureType.Defense.value -> R.layout.list_item_img_with_title
            else -> R.layout.list_item_empty_energy
        }

        val view = LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false)

        return when(viewType) {
            StructureType.Factory.value -> FactoryViewHolder(view, structuresList, planetWithUnits, myTeam)
            StructureType.Defense.value -> DefenseStructureViewHolder(view, structuresList, planetWithUnits, myTeam)
            else -> EmptyViewHolder(view)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val structure = structuresList[position]

        when(structure::class) {
            Factory::class -> {
                val factory = structure as Factory
                val factoryViewHolder = viewHolder as FactoryViewHolder
                factoryViewHolder.bindViewHolder(factory, planetIdsToPlanets[factory.deliverBuiltStructureToPlanetId])
            }
            DefenseStructure::class -> {
                val structure = structure as DefenseStructure
                val defenseViewHolder = viewHolder as DefenseStructureViewHolder
                defenseViewHolder.bindViewHolder(structure)
            }
            EMPTY_ENERGY::class -> {}
            else -> {}
        }
    }

    override fun getItemCount() =  structuresList.size

}
