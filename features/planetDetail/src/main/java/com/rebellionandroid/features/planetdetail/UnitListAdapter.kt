package com.rebellionandroid.features.planetdetail

import android.content.ClipData
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.rebellionandroid.components.commands.OrdersDialogFragment
import com.rebellionandroid.components.commands.enums.OrderDlgArgumentKeys
import com.rebellionandroid.components.commands.enums.OrderDlgComponentTypes
import com.rebellionandroid.components.commands.enums.OrderProcedures
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.database.gamestate.Personnel
import com.rebllelionandroid.core.database.gamestate.enums.UnitType
import kotlin.math.pow
import kotlin.math.sqrt

class UnitListAdapter(
    private val personnels: List<Personnel>,
    private val unitsAreTravelling: Boolean
) : RecyclerView.Adapter<UnitListAdapter.ViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    private class MyDragShadowBuilder(v: View) : View.DragShadowBuilder(v) {

        private val shadow = ColorDrawable(Color.LTGRAY)

        override fun onProvideShadowMetrics(size: Point, touch: Point) {
            val width: Int = view.width
            val height: Int = view.height
            shadow.setBounds(0, 0, width, height)
            size.set(width, height)
            touch.set(width / 2, height / 2)
        }

        override fun onDrawShadow(canvas: Canvas) {
            shadow.draw(canvas)
        }
    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    class ViewHolder(
        view: View,
        private val personnels: List<Personnel>,
        unitsAreTravelling: Boolean
        ) : RecyclerView.ViewHolder(view) {
        val unitLabel: TextView = view.findViewById(R.id.unit_label)
        val unitImg: ImageView = view.findViewById(R.id.unit_img)
        private var mVelocityTracker: VelocityTracker? = null
        private var isDragging:Boolean = false

        init {
            if(!unitsAreTravelling) {
                view.setOnTouchListener { v: View, event: MotionEvent ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            mVelocityTracker?.clear()
                            mVelocityTracker = mVelocityTracker ?: VelocityTracker.obtain()
                            mVelocityTracker?.addMovement(event)
                            true
                        }
                        MotionEvent.ACTION_UP -> {
                            mVelocityTracker?.recycle()
                            mVelocityTracker = null
                            if (isDragging) {
                                isDragging = false
                                true
                            } else {
                                v.performClick()
                                isDragging = false
                                false
                            }
                        }
                        MotionEvent.ACTION_MOVE -> {
                            mVelocityTracker?.apply {
                                val pointerId: Int = event.getPointerId(event.actionIndex)
                                addMovement(event)
                                computeCurrentVelocity(1000)
                                val dx = getXVelocity(pointerId).toDouble()
                                val dy = getYVelocity(pointerId).toDouble()
                                val vel = sqrt(dx.pow(2) + dy.pow(2))
                                if (vel > 150) {
                                    if (!isDragging) {
                                        val unit = personnels[adapterPosition]
                                        val dragData =
                                            ClipData.newPlainText("unit.id", unit.id.toString())
                                        val myShadow = MyDragShadowBuilder(itemView)
                                        v.startDrag(
                                            dragData,
                                            myShadow,
                                            null,
                                            0
                                        )
                                        isDragging = true
                                    }
                                }
                            }
                            isDragging
                        }
                        else -> false
                    }
                }

                view.setOnClickListener {
                    val personnel = personnels[adapterPosition]
                    if(personnel.unitType == UnitType.SpecialForces) {
                        val components = arrayListOf(
                            OrderDlgComponentTypes.SpecOpsMissionTypes.value,
                            OrderDlgComponentTypes.SpecOpsMissionTargets.value,
                            OrderDlgComponentTypes.PlanetPicker.value,
                        )
                        val bundle = bundleOf(
                            OrderDlgArgumentKeys.PersonnelId.value to personnel.id,
                            OrderDlgArgumentKeys.ComponentsToShow.value to components,
                            OrderDlgArgumentKeys.OrderProcedure.value to OrderProcedures.AssignMission
                        )
                        val fm: FragmentManager = (it.context as BaseActivity).supportFragmentManager
                        val editNameDialogFragment = OrdersDialogFragment.newInstance()
                        editNameDialogFragment.arguments = bundle
                        editNameDialogFragment.show(fm, "fragment_edit_name")
                    }
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_unit, viewGroup, false)
        return ViewHolder(view, personnels, unitsAreTravelling)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val unit = personnels[position]
        viewHolder.unitLabel.text = unit.unitType.value
        val imgSrc  = when(unit.unitType) {
            UnitType.Garrison -> R.drawable.personelle_garrison_outline
            UnitType.SpecialForces -> R.drawable.personelle_specops_outline
        }
        viewHolder.unitImg.setImageResource(imgSrc)
    }

    override fun getItemCount() =  personnels.size

}