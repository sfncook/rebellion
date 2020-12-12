package com.rebellionandroid.features.planetdetail

import android.content.ClipData
import android.graphics.Color
import android.os.Build
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.database.gamestate.ShipWithUnits
import com.rebllelionandroid.core.database.gamestate.enums.ShipType

class ShipsWithUnitListAdapter(
    private val shipsWithUnits: List<ShipWithUnits>
) : RecyclerView.Adapter<ShipsWithUnitListAdapter.ViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    class ViewHolder(view: View, private val shipsWithUnits: List<ShipWithUnits>) : RecyclerView.ViewHolder(
        view
    ) {
        val shipLabel: TextView = view.findViewById(R.id.ship_with_units_label)
        val shipImg: ImageView = view.findViewById(R.id.ship_with_units_ship_img)
        val shipWithUnitsList: RecyclerView = view.findViewById(R.id.ship_with_units_list)

        // Creates a new drag event listener
        @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
        private val dragListen = View.OnDragListener { v, event ->

            // Handles each of the expected events
            when (event.action) {
                DragEvent.ACTION_DRAG_ENTERED -> {
                    println("ACTION_DRAG_ENTERED")
                    // Applies a green tint to the View. Return true; the return value is ignored.
//                    (v as? ImageView)?.setColorFilter(Color.GREEN)
                    v.findViewById<View>(R.id.ship_with_units_background).setBackgroundColor(Color.GREEN)

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate()
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    println("ACTION_DRAG_EXITED")
                    // Re-sets the color tint to blue. Returns true; the return value is ignored.
//                    (v as? ImageView)?.setColorFilter(Color.BLUE)
                    v.findViewById<View>(R.id.ship_with_units_background).setBackgroundColor(Color.WHITE)

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    println("ACTION_DROP")
                    // Gets the item containing the dragged data
                    val item: ClipData.Item = event.clipData.getItemAt(0)

                    // Gets the text data from the item.
                    val dragData = item.text

                    // Displays a message containing the dragged data.
                    println("Dragged data is $dragData")

                    // Turns off any color tints
                    (v as? ImageView)?.clearColorFilter()

                    // Invalidates the view to force a redraw
                    v.invalidate()

                    // Returns true. DragEvent.getResult() will return true.
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    v.findViewById<View>(R.id.ship_with_units_background).setBackgroundColor(Color.WHITE)

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate()
                    true
                }
                else -> true
            }
        }

        init {
            view.setOnClickListener {
                val shipWithUnits = shipsWithUnits[adapterPosition]
                // Open mission assignment fragment
            }
            view.setOnDragListener(dragListen)
        }
    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_ship_with_units, viewGroup, false)
        return ViewHolder(view, shipsWithUnits)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val shipWithUnits = shipsWithUnits[position]
        viewHolder.shipLabel.text = shipWithUnits.ship.shipType.value
        val imgSrc  = when(shipWithUnits.ship.shipType) {
            ShipType.Biremes -> R.drawable.ship_2
            ShipType.Triremes -> R.drawable.ship_3
            ShipType.Quadriremes -> R.drawable.ship_4
            ShipType.Quinqueremes -> R.drawable.ship_5
            ShipType.Hexaremes -> R.drawable.ship_6
            ShipType.Septiremes -> R.drawable.ship_7
            ShipType.Octeres -> R.drawable.ship_8
        }
        viewHolder.shipImg.setImageResource(imgSrc)

        viewHolder.shipWithUnitsList.adapter = UnitListAdapter(shipWithUnits.units)
    }

    override fun getItemCount() =  shipsWithUnits.size

}