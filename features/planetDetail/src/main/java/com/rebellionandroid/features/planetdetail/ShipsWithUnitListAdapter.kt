package com.rebellionandroid.features.planetdetail

import android.content.ClipData
import android.graphics.Color
import android.os.Build
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.rebellionandroid.components.commands.ShipMoveDialogFragment
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.database.gamestate.ShipWithUnits
import com.rebllelionandroid.core.database.gamestate.enums.ShipType

class ShipsWithUnitListAdapter(
    private val shipsWithUnits: List<ShipWithUnits>,
    private val gameStateViewModel: GameStateViewModel,
    private val currentGameStateId: Long
) : RecyclerView.Adapter<ShipsWithUnitListAdapter.ViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    class ViewHolder(
            view: View,
            private val shipsWithUnits: List<ShipWithUnits>,
            private val gameStateViewModel: GameStateViewModel,
            private val currentGameStateId: Long
        ) : RecyclerView.ViewHolder(view) {
        val shipLabel: TextView = view.findViewById(R.id.ship_with_units_label)
        val travellingLabel: TextView = view.findViewById(R.id.ship_with_units_travelling_label)
        val travellingEtaText: TextView = view.findViewById(R.id.ship_with_units_eta)
        val shipImg: ImageView = view.findViewById(R.id.ship_with_units_ship_img)
        val shipWithUnitsList: RecyclerView = view.findViewById(R.id.ship_with_units_list)
        val bgView: View = view.findViewById(R.id.ship_with_units_background)

        @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
        private val dragListen = View.OnDragListener { v, event ->
            val shipWithUnits = shipsWithUnits[adapterPosition]
            when (event.action) {
                DragEvent.ACTION_DRAG_ENTERED -> {
                    if(!shipWithUnits.ship.isTraveling) {
                        bgView.setBackgroundColor(Color.GREEN)
                        v.invalidate()
                        true
                    } else {
                        false
                    }
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    if(!shipWithUnits.ship.isTraveling) {
                        bgView.setBackgroundColor(ContextCompat.getColor(v.context, R.color.list_item_bg))
                        v.invalidate()
                        true
                    } else {
                        false
                    }
                }
                DragEvent.ACTION_DROP -> {
                    if(!shipWithUnits.ship.isTraveling) {
                        val item: ClipData.Item = event.clipData.getItemAt(0)
                        val dragData = item.text
                        gameStateViewModel.moveUnitToShip(
                            dragData.toString().toLong(),
                            shipWithUnits.ship.id,
                            currentGameStateId
                        )
                        true
                    } else {
                        false
                    }
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    if(!shipWithUnits.ship.isTraveling) {
                        bgView.setBackgroundColor(
                            ContextCompat.getColor(
                                v.context,
                                R.color.list_item_bg
                            )
                        )
                        v.invalidate()
                        true
                    } else {
                        false
                    }
                }
                else -> true
            }
        }

        init {
            view.setOnClickListener {
                val shipWithUnits = shipsWithUnits[adapterPosition]
                if(!shipWithUnits.ship.isTraveling) {
                    val bundle = bundleOf(
                        "shipId" to shipWithUnits.ship.id,
                        "currentGameStateId" to currentGameStateId
                    )
                    val fm: FragmentManager = (it.context as BaseActivity).supportFragmentManager
                    val shipMoveDialogFragment = ShipMoveDialogFragment()
                    shipMoveDialogFragment.arguments = bundle
                    shipMoveDialogFragment.show(fm, "shipMoveDialogFragment")
                }
            }
            view.setOnDragListener(dragListen)
        }
    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_ship_with_units, viewGroup, false)
        return ViewHolder(view, shipsWithUnits, gameStateViewModel, currentGameStateId)
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
        viewHolder.travellingLabel.visibility =  if(shipWithUnits.ship.isTraveling) VISIBLE else GONE
        viewHolder.travellingEtaText.text =  shipWithUnits.ship.dayArrival.toString()

        if(shipWithUnits.ship.isTraveling) {
            viewHolder.bgView.setBackgroundColor(
                ContextCompat.getColor(viewHolder.bgView.context, R.color.unit_travelling)
            )
        }

        viewHolder.shipWithUnitsList.adapter = UnitListAdapter(shipWithUnits.units, shipWithUnits.ship.isTraveling)
    }

    override fun getItemCount() =  shipsWithUnits.size

}