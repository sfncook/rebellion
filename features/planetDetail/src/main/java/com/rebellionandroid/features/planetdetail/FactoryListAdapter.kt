package com.rebellionandroid.features.planetdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.database.gamestate.Factory
import com.rebllelionandroid.core.database.gamestate.enums.FactoryType

class FactoryListAdapter(
    private val factories: List<Factory>
) : RecyclerView.Adapter<FactoryListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val factories: List<Factory>) : RecyclerView.ViewHolder(
        view
    ) {
        val factoryLabel: TextView = view.findViewById(R.id.factory_label)
        val factoryImg: ImageView = view.findViewById(R.id.factory_img)

        init {
            view.setOnClickListener {
                val factory = factories[adapterPosition]
                // Open factory build modal
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_factory, viewGroup, false)
        return ViewHolder(view, factories)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val factory = factories[position]
        viewHolder.factoryLabel.text = factory.factoryType.value
        val imgSrc  = when(factory.factoryType) {
            FactoryType.ConstructionYard -> R.drawable.factory_ctor_yard
            FactoryType.ShipYard -> R.drawable.factory_ship_yard
            FactoryType.TrainingFaciliy -> R.drawable.factory_training_facility
        }
        viewHolder.factoryImg.setImageResource(imgSrc)
    }

    override fun getItemCount() =  factories.size

}
