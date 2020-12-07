package com.rebellionandroid.features.planetdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class EmptyEnergyListAdapter(
    private val emptyEnergies: List<Int>
) : RecyclerView.Adapter<EmptyEnergyListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val emptyEnergies: List<Int>) : RecyclerView.ViewHolder(
        view
    ) {
        val emptyImg: ImageView = view.findViewById(R.id.empty_img)
        init {
            view.setOnClickListener {
                // Open factory build modal
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_empty_energy, viewGroup, false)
        return ViewHolder(view, emptyEnergies)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//        viewHolder.emptyImg.setImageResource(R.drawable.energy_empty)
    }

    override fun getItemCount() =  emptyEnergies.size

}
