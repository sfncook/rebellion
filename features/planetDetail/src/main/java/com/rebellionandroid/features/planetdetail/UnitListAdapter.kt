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
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.rebellionandroid.components.commands.UnitCmdDialogFragment
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.database.gamestate.Unit
import com.rebllelionandroid.core.database.gamestate.enums.UnitType
import kotlin.math.pow
import kotlin.math.sqrt

class UnitListAdapter(
    private val units: List<Unit>
) : RecyclerView.Adapter<UnitListAdapter.ViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    private class MyDragShadowBuilder(v: View) : View.DragShadowBuilder(v) {

        private val shadow = ColorDrawable(Color.LTGRAY)

        // Defines a callback that sends the drag shadow dimensions and touch point back to the
        // system.
        override fun onProvideShadowMetrics(size: Point, touch: Point) {
            // Sets the width of the shadow to half the width of the original View
            val width: Int = view.width

            // Sets the height of the shadow to half the height of the original View
            val height: Int = view.height

            // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
            // Canvas that the system will provide. As a result, the drag shadow will fill the
            // Canvas.
            shadow.setBounds(0, 0, width, height)

            // Sets the size parameter's width and height values. These get back to the system
            // through the size parameter.
            size.set(width, height)

            // Sets the touch point's position to be in the middle of the drag shadow
            touch.set(width / 2, height / 2)
        }

        // Defines a callback that draws the drag shadow in a Canvas that the system constructs
        // from the dimensions passed in onProvideShadowMetrics().
        override fun onDrawShadow(canvas: Canvas) {
            // Draws the ColorDrawable in the Canvas passed in from the system.
            shadow.draw(canvas)
        }
    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    class ViewHolder(view: View, private val units: List<Unit>) : RecyclerView.ViewHolder(
        view
    ) {
        val unitLabel: TextView = view.findViewById(R.id.unit_label)
        val unitImg: ImageView = view.findViewById(R.id.unit_img)
        private var mVelocityTracker: VelocityTracker? = null
        private var isDragging:Boolean = false

        init {
            view.setOnTouchListener { v:View, event:MotionEvent ->
                when(event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        println("ACTION_DOWN")

                        mVelocityTracker?.clear()
                        mVelocityTracker = mVelocityTracker ?: VelocityTracker.obtain()
                        mVelocityTracker?.addMovement(event)

                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        println("ACTION_UP")
                        mVelocityTracker?.recycle()
                        mVelocityTracker = null
                        if(isDragging) {
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
                            // When you want to determine the velocity, call
                            // computeCurrentVelocity(). Then call getXVelocity()
                            // and getYVelocity() to retrieve the velocity for each pointer ID.
                            computeCurrentVelocity(1000)
                            // Log velocity of pixels per second
                            // Best practice to use VelocityTrackerCompat where possible.
                            val dx = getXVelocity(pointerId).toDouble()
                            val dy = getYVelocity(pointerId).toDouble()
//                            println("X velocity: ${dx}")
//                            println("Y velocity: ${dy}")
                            val vel = sqrt(dx.pow(2) + dy.pow(2))
                            println("Velocity: $vel")
                            if(vel>150) {
                                if(!isDragging) {
                                    println("ACTION_MOVE start Dragging")
                                    val unit = units[adapterPosition]
                                    val dragData = ClipData.newPlainText("unit.id", unit.id.toString())
                                    val myShadow = MyDragShadowBuilder(itemView)
                                    v.startDrag(
                                        dragData,   // the data to be dragged
                                        myShadow,   // the drag shadow builder
                                        null,       // no need to use local data
                                        0           // flags (not currently used, set to 0)
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
//                val unit = units[adapterPosition]
                // Open mission assignment fragment
                val fm: FragmentManager = (it.context as BaseActivity).supportFragmentManager
                val editNameDialogFragment = UnitCmdDialogFragment()
                editNameDialogFragment.show(fm, "fragment_edit_name")
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_unit, viewGroup, false)
        return ViewHolder(view, units)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val unit = units[position]
        viewHolder.unitLabel.text = unit.unitType.value
        val imgSrc  = when(unit.unitType) {
            UnitType.Garrison -> R.drawable.personelle_garrison
            UnitType.SpecialForces -> R.drawable.personelle_specops
        }
        viewHolder.unitImg.setImageResource(imgSrc)
    }

    override fun getItemCount() =  units.size

}