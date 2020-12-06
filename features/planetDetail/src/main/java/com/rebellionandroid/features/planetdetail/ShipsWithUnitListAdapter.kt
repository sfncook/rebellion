package com.rebellionandroid.features.planetdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.database.gamestate.Ship
import com.rebllelionandroid.core.database.gamestate.Unit
import com.rebllelionandroid.core.database.gamestate.enums.ShipType
import com.rebllelionandroid.core.database.gamestate.enums.UnitType

class ShipsWithUnitListAdapter(
    private val ships: List<Ship>
) : RecyclerView.Adapter<ShipsWithUnitListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val ships: List<Ship>) : RecyclerView.ViewHolder(
        view
    ) {
        val shipLabel: TextView = view.findViewById(R.id.ship_with_units_label)
        val shipImg: ImageView = view.findViewById(R.id.ship_with_units_ship_img)
        val shipWithUnitsList: RecyclerView = view.findViewById(R.id.ship_with_units_list)

        init {
            view.setOnClickListener {
                val ship = ships[adapterPosition]
                // Open mission assignment fragment
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_ship_with_units, viewGroup, false)
        return ViewHolder(view, ships)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val ship = ships[position]
        viewHolder.shipLabel.text = ship.shipType.value
        val imgSrc  = when(ship.shipType) {
            ShipType.Biremes -> R.drawable.ship_2
            ShipType.Triremes -> R.drawable.ship_3
            ShipType.Quadriremes -> R.drawable.ship_4
            ShipType.Quinqueremes -> R.drawable.ship_5
            ShipType.Hexaremes -> R.drawable.ship_6
            ShipType.Septiremes -> R.drawable.ship_7
            ShipType.Octeres -> R.drawable.ship_8
        }
        viewHolder.shipImg.setImageResource(imgSrc)

//        viewHolder.shipWithUnitsList.adapter = UnitListAdapter(units)
    }

    override fun getItemCount() =  ships.size

}