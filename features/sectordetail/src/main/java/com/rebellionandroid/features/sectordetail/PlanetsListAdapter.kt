package com.rebellionandroid.features.sectordetail

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rebellionandroid.features.planetdetail.PlanetDetailActivity
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.features.sectorsdetail.R


class PlanetsListAdapter(
    private val planetsWithUnits: List<PlanetWithUnits>
) : RecyclerView.Adapter<PlanetsListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val planetsWithUnits: List<PlanetWithUnits>) : RecyclerView.ViewHolder(
        view
    ) {
        val planetName: TextView = view.findViewById(R.id.planet_name)
        val planetLoyaltyImg: ImageView = view.findViewById(R.id.planet_loyalty)

        init {
            view.setOnClickListener {
                val planetWithUnits = planetsWithUnits[adapterPosition]
                println("click planet ${planetWithUnits.planet.name}")
                val intent = Intent(it.context, PlanetDetailActivity::class.java)
                intent.putExtra("SELECTED_PLANET_ID", planetWithUnits.planet.id);
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_planet, viewGroup, false)
        return ViewHolder(view, planetsWithUnits)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val planet = planetsWithUnits[position].planet
        viewHolder.planetName.text = planet.name

        val (imgId, colorId) = Utilities.getLoyaltyIconForPlanet(planet)
        viewHolder.planetLoyaltyImg.setImageResource(imgId)
        viewHolder.planetLoyaltyImg.setColorFilter(
                ContextCompat.getColor(
                        viewHolder.itemView.context,
                        colorId
                ), android.graphics.PorterDuff.Mode.MULTIPLY
        )
    }

    override fun getItemCount() =  planetsWithUnits.size

}