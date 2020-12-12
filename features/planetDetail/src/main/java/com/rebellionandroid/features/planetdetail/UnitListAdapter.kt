package com.rebellionandroid.features.planetdetail

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.rebellionandroid.components.commands.UnitCmdDialogFragment
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.database.gamestate.Unit
import com.rebllelionandroid.core.database.gamestate.enums.UnitType

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
            val width: Int = view.width / 2

            // Sets the height of the shadow to half the height of the original View
            val height: Int = view.height / 2

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

        init {
            view.setOnClickListener {
                println("short click")
                val unit = units[adapterPosition]
                // Open mission assignment fragment
                val fm: FragmentManager = (it.context as BaseActivity).supportFragmentManager
                val editNameDialogFragment = UnitCmdDialogFragment()
                editNameDialogFragment.show(fm, "fragment_edit_name")
            }

            view.setOnLongClickListener { v: View ->
                val unit = units[adapterPosition]
                println("long click unit.id:"+unit.id)
                // Create a new ClipData.
                // This is done in two steps to provide clarity. The convenience method
                // ClipData.newPlainText() can create a plain text ClipData in one step.

//                // Create a new ClipData.Item from the ImageView object's tag
//                val item = ClipData.Item(v.tag as? CharSequence)
//
//                // Create a new ClipData using the tag as a label, the plain text MIME type, and
//                // the already-created item. This will create a new ClipDescription object within the
//                // ClipData, and set its MIME type entry to "text/plain"
//                val dragData = ClipData(
//                    v.tag as? CharSequence,
//                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
//                    item)

                val dragData = ClipData.newPlainText("unit.id", unit.id.toString())

                // Instantiates the drag shadow builder.
                val myShadow = MyDragShadowBuilder(this.itemView)

                // Starts the drag
                v.startDrag(
                    dragData,   // the data to be dragged
                    myShadow,   // the drag shadow builder
                    null,       // no need to use local data
                    0           // flags (not currently used, set to 0)
                )
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