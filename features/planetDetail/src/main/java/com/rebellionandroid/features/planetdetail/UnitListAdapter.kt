package com.rebellionandroid.features.planetdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.database.gamestate.Unit
import com.rebllelionandroid.core.database.gamestate.enums.UnitType

class UnitListAdapter(
    private val units: List<Unit>
) : RecyclerView.Adapter<UnitListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val units: List<Unit>) : RecyclerView.ViewHolder(
        view
    ) {
        val unitLabel: TextView = view.findViewById(R.id.unit_label)
        val unitImg: ImageView = view.findViewById(R.id.unit_img)

        init {
            view.setOnClickListener {
                val unit = units[adapterPosition]
                // Open mission assignment fragment
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_unit, viewGroup, false)
        return ViewHolder(view, units)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val unit = units[position]
        viewHolder.unitLabel.text = unit.unitType.value
        val imgSrc  = when(unit.unitType) {
            UnitType.Garrison -> R.drawable.personelle_garrison
            UnitType.SpecialForces -> R.drawable.personelle_specops
        }
        viewHolder.unitImg.setImageResource(imgSrc)
    }

    override fun getItemCount() =  units.size

}