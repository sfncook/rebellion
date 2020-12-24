package com.rebellionandroid.components.entityUi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits

class SectorItemPlanetsListAdapter(
        private val planets: List<PlanetWithUnits>
) : RecyclerView.Adapter<SectorItemPlanetsListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val planetLoyaltyImg: ImageView = view.findViewById(R.id.planet_loyalty_img)

        init {
            view.setOnClickListener {
                // This sucks, but i'm not sure how else to fix it
                val parent = it.parent.parent.parent as View
                parent.performClick()
            }
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
        val (imgId, colorId) = Utilities.getLoyaltyIconForPlanet(planet)
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
