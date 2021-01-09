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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.commands.CommandUtilities
import com.rebellionandroid.components.commands.OrdersDialogFragment
import com.rebellionandroid.components.commands.enums.OrderDlgArgumentKeys
import com.rebellionandroid.components.commands.enums.OrderDlgComponentTypes
import com.rebellionandroid.components.commands.enums.OrderProcedures
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.database.gamestate.ShipWithUnits
import com.rebllelionandroid.core.database.gamestate.enums.ShipType

class ShipsWithUnitListAdapter(
    private val shipsWithUnits: List<ShipWithUnits>,
    private val gameStateViewModel: GameStateViewModel,
    private val currentGameStateId: Long,
    private val parentFragment: Fragment
) : RecyclerView.Adapter<ShipsWithUnitListAdapter.ViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    class ViewHolder(
            view: View,
            private val shipsWithUnits: List<ShipWithUnits>,
            private val gameStateViewModel: GameStateViewModel,
            private val currentGameStateId: Long,
            private val parentFragment: Fragment
        ) : RecyclerView.ViewHolder(view) {
        val shipLabel: TextView = view.findViewById(R.id.ship_with_units_label)
        val healthPointsText: TextView = view.findViewById(R.id.ship_with_units_healthpoints)
        val travellingEtaLabel: TextView = view.findViewById(R.id.ship_with_units_eta_label)
        val travellingEtaText: TextView = view.findViewById(R.id.ship_with_units_eta)
        val shipImg: ImageView = view.findViewById(R.id.ship_with_units_ship_img)
        val travellingIcon: ImageView = view.findViewById(R.id.ship_with_units_travelling_icon)
        val shipWithUnitsList: RecyclerView = view.findViewById(R.id.ship_with_units_list)
        val bgView: View = view.findViewById(R.id.ship_with_units_background)
        val capcityText: TextView = view.findViewById(R.id.ship_with_units_capacity)

        @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
        private val dragListen = View.OnDragListener { v, event ->
            val shipWithUnits = shipsWithUnits[adapterPosition]
            when (event.action) {
                DragEvent.ACTION_DRAG_ENTERED -> {
                    val shipHasAvailability = shipWithUnits.ship.unitCapacity > shipWithUnits.personnels.size
                    if(!shipWithUnits.ship.isTraveling && shipHasAvailability) {
                        bgView.setBackgroundColor(Color.GREEN)
                        v.invalidate()
                        true
                    } else {
                        false
                    }
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    val shipHasAvailability = shipWithUnits.ship.unitCapacity > shipWithUnits.personnels.size
                    if(!shipWithUnits.ship.isTraveling && shipHasAvailability) {
                        bgView.setBackgroundColor(ContextCompat.getColor(v.context, R.color.list_item_bg))
                        v.invalidate()
                        true
                    } else {
                        false
                    }
                }
                DragEvent.ACTION_DROP -> {
                    val shipHasAvailability = shipWithUnits.ship.unitCapacity > shipWithUnits.personnels.size
                    if(!shipWithUnits.ship.isTraveling && shipHasAvailability) {
                        val item: ClipData.Item = event.clipData.getItemAt(0)
                        val dragData = item.text
                        val unitId = dragData.toString().toLong()
                        CommandUtilities.moveUnitToShip(gameStateViewModel, unitId, shipWithUnits.ship.id, currentGameStateId)
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
                    val components = arrayListOf(OrderDlgComponentTypes.PlanetPicker.value)
                    val bundle = bundleOf(
                        OrderDlgArgumentKeys.MoveShipId.value to shipWithUnits.ship.id,
                        OrderDlgArgumentKeys.ComponentsToShow.value to components,
                        OrderDlgArgumentKeys.OrderProcedure.value to OrderProcedures.MoveShip
                    )
                    val fm: FragmentManager = (it.context as BaseActivity).supportFragmentManager
                    val shipMoveDialogFragment = OrdersDialogFragment.newInstance()
                    shipMoveDialogFragment.setTargetFragment(parentFragment, 1234)
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
        return ViewHolder(view, shipsWithUnits, gameStateViewModel, currentGameStateId, parentFragment)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val shipWithUnits = shipsWithUnits[position]
        viewHolder.shipLabel.text = shipWithUnits.ship.shipType.value
        viewHolder.healthPointsText.text = shipWithUnits.ship.healthPoints.toString()
        val imgSrc  = when(shipWithUnits.ship.shipType) {
            ShipType.Bireme -> R.drawable.ship_2
            ShipType.Trireme -> R.drawable.ship_3
            ShipType.Quadrireme -> R.drawable.ship_4
            ShipType.Quinquereme -> R.drawable.ship_5
            ShipType.Hexareme -> R.drawable.ship_6
            ShipType.Septireme -> R.drawable.ship_7
            ShipType.Octere -> R.drawable.ship_8
        }
        viewHolder.shipImg.setImageResource(imgSrc)
        viewHolder.travellingIcon.visibility =  if(shipWithUnits.ship.isTraveling) VISIBLE else GONE
        viewHolder.travellingEtaLabel.visibility =  if(shipWithUnits.ship.isTraveling) VISIBLE else GONE
        viewHolder.travellingEtaText.visibility =  if(shipWithUnits.ship.isTraveling) VISIBLE else GONE
        viewHolder.travellingEtaText.text =  shipWithUnits.ship.dayArrival.toString()
        viewHolder.capcityText.text =  shipWithUnits.ship.unitCapacity.toString()

        if(shipWithUnits.ship.isTraveling) {
            viewHolder.bgView.setBackgroundColor(
                ContextCompat.getColor(viewHolder.bgView.context, R.color.unit_travelling)
            )
        }

        viewHolder.shipWithUnitsList.adapter = UnitListAdapter(shipWithUnits.personnels, shipWithUnits.ship.isTraveling)
    }

    override fun getItemCount() =  shipsWithUnits.size

}