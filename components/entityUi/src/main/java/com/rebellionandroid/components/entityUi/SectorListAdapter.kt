package com.rebellionandroid.components.entityUi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.SectorWithPlanets
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty

class SectorListAdapter(
    private val allSectorsWithPlanets: List<SectorWithPlanets>,
    private val myTeam: TeamLoyalty,
    private val onSelectCallback: (sectorId: Long) -> Unit
) : RecyclerView.Adapter<SectorListAdapter.ViewHolder>() {

    class ViewHolder(
        view: View,
        private val sectorsWithPlanets: List<SectorWithPlanets>,
        private val onSelectCallback: (sectorId: Long) -> Unit
        ) : RecyclerView.ViewHolder(
        view
    ) {
        val sectorName: TextView = view.findViewById(R.id.sector_name)
        val planetsList: RecyclerView  = view.findViewById(R.id.planets_list)

        val manyShipsInSectorTxt: TextView = view.findViewById(R.id.many_ships_in_sector_txt)
        val shipsInSectorImg: ImageView = view.findViewById(R.id.ships_in_sector_img)

        val manyEnemyShipsInSectorTxt: TextView = view.findViewById(R.id.many_enemy_ships_in_sector_txt)
        val enemyShipsInSectorImg: ImageView = view.findViewById(R.id.enemy_ships_in_sector_img)

        init {
            view.setOnClickListener {
                val sectorWithPlanets = sectorsWithPlanets[adapterPosition]
                val sectorId = sectorWithPlanets.sector.id
                onSelectCallback(sectorId)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_sector, viewGroup, false)
        return ViewHolder(view, allSectorsWithPlanets, onSelectCallback)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val sector = allSectorsWithPlanets[position].sector
        viewHolder.sectorName.text = sector.name

        val planets = allSectorsWithPlanets[position].planets
        val viewAdapter = SectorItemPlanetsListAdapter(Utilities.sortPlanets(planets))
        viewHolder.planetsList.adapter = viewAdapter
        viewHolder.planetsList.apply {
            adapter = viewAdapter
        }

        Utilities.populateShipsInSectorUi(
            allSectorsWithPlanets[position],
            viewHolder.manyShipsInSectorTxt,
            viewHolder.shipsInSectorImg,
            viewHolder.manyEnemyShipsInSectorTxt,
            viewHolder.enemyShipsInSectorImg
        )
    }

    override fun getItemCount() =  allSectorsWithPlanets.size

}