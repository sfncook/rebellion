package com.rebellionandroid.features.newgameactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.database.gamestate.GameState
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class NewGameFragment: Fragment() {

    private lateinit var gameStateViewModel: GameStateViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_new_game, container, false)

        gameStateViewModel = (activity as BaseActivity).gameStateViewModel
        updateList(root)

        root.findViewById<MaterialButton>(R.id.btn_create_new_game).setOnClickListener {
            gameStateViewModel.createNewGameState() {
                updateList(root)
            }
        }

        return root
    }

    private fun updateList(view: View) {
        gameStateViewModel.getAllGameStates { allGameStates ->
//            val sortedGameStates = it.toSortedSet(Comparator { s1, s2 ->
//                s1.gameTime.compareTo(s2.gameTime)
//            })
//            val sortedGameStatesArr = ArrayList(sortedGameStates)
            val sortedGameStatesArr = allGameStates.sortedBy { gameState -> gameState.gameStartedTime }
            val viewAdapter = GameListAdapter(sortedGameStatesArr)
//            val viewAdapter = GameListAdapter(it)
            val listGames = view.findViewById<RecyclerView>(R.id.list_games)
            viewLifecycleOwner.lifecycleScope.launch {
                listGames.adapter = viewAdapter
            }
        }
    }
}
