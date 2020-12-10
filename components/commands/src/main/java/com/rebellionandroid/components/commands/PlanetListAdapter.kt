package com.rebellionandroid.components.commands

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.Planet

class PlanetListAdapter(
    private val planets: List<Planet>
) : RecyclerView.Adapter<PlanetListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val planets: List<Planet>) : RecyclerView.ViewHolder(
        view
    ) {
        val label: TextView = view.findViewById(R.id.unit_label)
        val img: ImageView = view.findViewById(R.id.unit_img)

        init {
            view.setOnClickListener {
                val planet = planets[adapterPosition]
                // Open factory build modal
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_img_with_title, viewGroup, false)
        return ViewHolder(view, planets)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val planet = planets[position]
        viewHolder.label.text = planet.name
        val (imgId, colorId) = Utilities.getLoyaltyIconForPlanet(planet)
        viewHolder.img.setImageResource(imgId)
        viewHolder.img.setColorFilter(
            ContextCompat.getColor(viewHolder.itemView.context, colorId), android.graphics.PorterDuff.Mode.MULTIPLY
        )
//        viewHolder.factoryLabel.text = factory.factoryType.value
//        val imgSrc  = when(factory.factoryType) {
//            FactoryType.ConstructionYard -> R.drawable.factory_ctor_yard
//            FactoryType.ShipYard -> R.drawable.factory_ship_yard
//            FactoryType.TrainingFaciliy -> R.drawable.factory_training_facility
//        }
//        viewHolder.factoryImg.setImageResource(imgSrc)
    }

    override fun getItemCount() =  planets.size

}
