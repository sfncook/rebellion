package com.rebellionandroid.components.sectorWithPlanetsFragment

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.GameState
import com.rebllelionandroid.core.database.gamestate.SectorWithPlanets
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty

class SectorListAdapter(
    private val sectors: List<SectorWithPlanets>,
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
        val enemShipsInSectorImg: ImageView = view.findViewById(R.id.enemy_ships_in_sector_img)

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
        return ViewHolder(view, sectors, onSelectCallback)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val sector = sectors[position].sector
        viewHolder.sectorName.text = sector.name

        val planets = sectors[position].planets
        val viewAdapter = SectorItemPlanetsListAdapter(Utilities.sortPlanets(planets))
        viewHolder.planetsList.adapter = viewAdapter
        viewHolder.planetsList.apply {
            adapter = viewAdapter
        }

        var manyMyShips = 0
        var manyEnemyShips = 0
        planets.forEach { planetWithUnits ->
            val myShips = planetWithUnits.shipsWithUnits.filter { shipWithUnits -> shipWithUnits.ship.team == myTeam }
            val enemyShips = planetWithUnits.shipsWithUnits.filter { shipWithUnits -> shipWithUnits.ship.team != myTeam }
            manyMyShips += myShips.size
            manyEnemyShips += enemyShips.size
        }
        viewHolder.manyShipsInSectorTxt.text = manyMyShips.toString()
        viewHolder.manyEnemyShipsInSectorTxt.text = manyEnemyShips.toString()

        val myColor = when(myTeam) {
            TeamLoyalty.TeamA -> R.color.loyalty_team_a
            TeamLoyalty.TeamB -> R.color.loyalty_team_b
            else -> R.color.loyalty_neutral
        }
        val enemyColor = when(myTeam) {
            TeamLoyalty.TeamA -> R.color.loyalty_team_b
            TeamLoyalty.TeamB -> R.color.loyalty_team_a
            else -> R.color.loyalty_neutral
        }
        viewHolder.shipsInSectorImg.setColorFilter(
            ContextCompat.getColor(viewHolder.shipsInSectorImg.context, myColor), android.graphics.PorterDuff.Mode.MULTIPLY
        )
        viewHolder.enemShipsInSectorImg.setColorFilter(
            ContextCompat.getColor(viewHolder.shipsInSectorImg.context, enemyColor), android.graphics.PorterDuff.Mode.MULTIPLY
        )
    }

    override fun getItemCount() =  sectors.size

}