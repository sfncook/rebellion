package com.rebellionandroid.features.newgameactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.database.gamestate.GameState

class GameListAdapter(
        private val gameStates: List<GameState>,
        private val navController: NavController
) : RecyclerView.Adapter<GameListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val gameStates: List<GameState>, navController: NavController) : RecyclerView.ViewHolder(
            view
    ) {
        val textGameId: TextView = view.findViewById(R.id.text_game_id)
        val textGameStartTime: TextView = view.findViewById(R.id.text_game_start_time)

        init {
            view.setOnClickListener {
                navController.navigate(R.id.second_graph)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.list_item_game, viewGroup, false)
        return ViewHolder(view, gameStates, navController)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val gameState = gameStates[position]
        viewHolder.textGameId.text = "Game #:${gameState.id.toString()}"
        viewHolder.textGameStartTime.text = gameState.gameStartedTime.toString()
    }

    override fun getItemCount() = gameStates.size
}