package com.rebellionandroid.features.newgameactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.database.gamestate.GameState

class GameListAdapter(
        private val gameStates: List<GameState>
) : RecyclerView.Adapter<GameListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val gameStates: List<GameState>) : RecyclerView.ViewHolder(
            view
    ) {
        val textGameId: TextView = view.findViewById(R.id.text_game_id)

        init {
            view.setOnClickListener {
//                val sectorWithPlanets = sectorsWithPlanets[adapterPosition]
//                println("click sector ${sectorWithPlanets.sector.name}")
//                val intent = Intent(it.context, SectorDetailActivity::class.java)
//                intent.putExtra("SELECTED_SECTOR_ID", sectorWithPlanets.sector.id);
//                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.list_item_game, viewGroup, false)
        return ViewHolder(view, gameStates)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val gameState = gameStates[position]
        viewHolder.textGameId.text = "Game #:${gameState.id.toString()}"
    }

    override fun getItemCount() = gameStates.size
}