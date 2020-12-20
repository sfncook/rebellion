package com.rebellionandroid.features.planetdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.database.gamestate.ShipWithUnits
import com.rebllelionandroid.core.database.gamestate.enums.ShipType
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty

class EnemyShipsListAdapter(private val enemyShipsWithUnits: List<ShipWithUnits>)
    : RecyclerView.Adapter<EnemyShipsListAdapter.ViewHolder>() {

    class ViewHolder(view: View)
        : RecyclerView.ViewHolder(view) {
        val imgView: ImageView = view.findViewById(R.id.img)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_img, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val enemyShipWithUnits = enemyShipsWithUnits[position]
        val imgSrc  = when(enemyShipWithUnits.ship.shipType) {
            ShipType.Bireme -> R.drawable.ship_2
            ShipType.Trireme -> R.drawable.ship_3
            ShipType.Quadrireme -> R.drawable.ship_4
            ShipType.Quinquereme -> R.drawable.ship_5
            ShipType.Hexareme -> R.drawable.ship_6
            ShipType.Septireme -> R.drawable.ship_7
            ShipType.Octere -> R.drawable.ship_8
            else -> R.drawable.ship_2
        }
        viewHolder.imgView.setImageResource(imgSrc)

        val enemyColor = when(enemyShipWithUnits.ship.team) {
            TeamLoyalty.TeamA -> R.color.loyalty_team_a
            TeamLoyalty.TeamB -> R.color.loyalty_team_b
            else -> R.color.loyalty_neutral
        }
        viewHolder.imgView.setColorFilter(
            ContextCompat.getColor(viewHolder.imgView.context, enemyColor), android.graphics.PorterDuff.Mode.MULTIPLY
        )
    }

    override fun getItemCount() = enemyShipsWithUnits.size
}
