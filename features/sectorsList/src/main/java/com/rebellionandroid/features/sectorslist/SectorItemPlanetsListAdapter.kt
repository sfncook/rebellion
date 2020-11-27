package com.rebellionandroid.features.sectorslist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.gamestate.SectorWithPlanets
import com.rebllelionandroid.features.sectorsList.R

class SectorItemPlanetsListAdapter(
        private val planets: List<PlanetWithUnits>
) : RecyclerView.Adapter<SectorItemPlanetsListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val planetLoyaltyImg: ImageView = view.findViewById(R.id.planet_loyalty_img)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.list_item_sector_item_planet, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val (planet, _) = planets[position]
        val loyaltyDiff = planet.teamALoyalty - planet.teamBLoyalty
        var imgId: Int
        var colorId: Int
        if( 40 < loyaltyDiff) {
            imgId = R.drawable.loyalty_lg
            colorId = R.color.loyalty_team_a
        } else if( 10 < loyaltyDiff) {
            imgId = R.drawable.loyalty_sm
            colorId = R.color.loyalty_team_a
        } else if( -40 > loyaltyDiff) {
            imgId = R.drawable.loyalty_lg
            colorId = R.color.loyalty_team_b
        } else if( -10 > loyaltyDiff) {
            imgId = R.drawable.loyalty_sm
            colorId = R.color.loyalty_team_b
        } else {
            imgId = R.drawable.loyalty_lg
            colorId = R.color.loyalty_neutral
        }
        viewHolder.planetLoyaltyImg.setImageResource(imgId)
        viewHolder.planetLoyaltyImg.setColorFilter(
            ContextCompat.getColor(
                viewHolder.itemView.context,
                colorId
            ), android.graphics.PorterDuff.Mode.MULTIPLY
        )
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() =  planets.size

}
