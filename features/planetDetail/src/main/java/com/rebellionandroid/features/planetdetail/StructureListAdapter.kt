package com.rebellionandroid.features.planetdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.rebellionandroid.components.commands.FactoryBuildDialogFragment
import com.rebellionandroid.components.commands.ShipMoveDialogFragment
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.database.gamestate.DefenseStructure
import com.rebllelionandroid.core.database.gamestate.Factory
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.gamestate.enums.DefenseStructureType
import com.rebllelionandroid.core.database.gamestate.enums.FactoryType

class StructureListAdapter(
    planetWithUnits: PlanetWithUnits,
    private val currentGameStateId: Long
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

    class FactoryViewHolder(
        view: View,
        structureList: List<Any>,
        private val currentGameStateId: Long
    ) : RecyclerView.ViewHolder(view) {
        val factoryLabel: TextView = view.findViewById(R.id.factory_label)
        val factoryImg: ImageView = view.findViewById(R.id.factory_img)

        init {
            view.setOnClickListener {
                val structureObj = structureList[adapterPosition]
                if(structureObj::class == Factory::class) {
                    val factory = structureObj as Factory
                    val bundle = bundleOf(
                        "factoryId" to factory.id,
                        "currentGameStateId" to currentGameStateId
                    )
                    val fm: FragmentManager = (it.context as BaseActivity).supportFragmentManager
                    val factoryBuildDialogFragment = FactoryBuildDialogFragment()
                    factoryBuildDialogFragment.arguments = bundle
                    factoryBuildDialogFragment.show(fm, "shipMoveDialogFragment")
                } else {
                    println("Whoops! the FactoryViewHolder got a click event for an object that is not of type Factory: ${structureObj::class.qualifiedName}")
                }
            }
        }
    }

    class DefenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label: TextView = view.findViewById(R.id.unit_label)
        val img: ImageView = view.findViewById(R.id.unit_img)
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
            StructureType.Factory.value -> FactoryViewHolder(view, structuresList, currentGameStateId)
            StructureType.Defense.value -> DefenseViewHolder(view)
            else -> EmptyViewHolder(view)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val structure = structuresList[position]

        when(structure::class) {
            Factory::class -> {
                val factory = structure as Factory
                val factoryViewHolder = viewHolder as FactoryViewHolder
                factoryViewHolder.factoryLabel.text = factory.factoryType.value
                val imgSrc  = when(factory.factoryType) {
                    FactoryType.ConstructionYard -> R.drawable.factory_ctor_yard
                    FactoryType.ShipYard -> R.drawable.factory_ship_yard
                    FactoryType.TrainingFaciliy -> R.drawable.factory_training_facility
                }
                factoryViewHolder.factoryImg.setImageResource(imgSrc)
            }
            DefenseStructure::class -> {
                val defenseStructure = structure as DefenseStructure
                val defenseViewHolder = viewHolder as DefenseViewHolder
                defenseViewHolder.label.text = defenseStructure.defenseStructureType.value
                val imgSrc  = when(defenseStructure.defenseStructureType) {
                    DefenseStructureType.PlanetaryShield -> R.drawable.planetary_shield
                    DefenseStructureType.OrbitalBattery -> R.drawable.orbital_battery
                }
                defenseViewHolder.img.setImageResource(imgSrc)
            }
            EMPTY_ENERGY::class -> {}
            else -> {}
        }
    }

    override fun getItemCount() =  structuresList.size

}
