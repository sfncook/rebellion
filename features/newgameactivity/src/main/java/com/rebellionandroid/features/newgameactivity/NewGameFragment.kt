package com.rebellionandroid.features.newgameactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

//        gameStateViewModel = (activity as BaseActivity).gameStateViewModel
//        gameStateViewModel.getAllGameStates {
//            println("Many games:${it.size}")
//            val viewAdapter = GameListAdapter(it)
//            val listGames = root.findViewById<RecyclerView>(R.id.list_games)
//            listGames.adapter = viewAdapter
//        }

        val rvContacts = root.findViewById<RecyclerView>(R.id.list_games)
        // Initialize contacts
        val contacts = ArrayList<GameState>()
        contacts.add(GameState(Random.nextLong(), false, 0, TeamLoyalty.TeamA))
        contacts.add(GameState(Random.nextLong(), false, 999, TeamLoyalty.TeamA))
        contacts.add(GameState(Random.nextLong(), false, 8888, TeamLoyalty.TeamA))
        contacts.add(GameState(Random.nextLong(), false, 77777, TeamLoyalty.TeamA))

        // Create adapter passing in the sample user data
        val adapter = GameListAdapter(contacts)

        // Attach the adapter to the recyclerview to populate items
        rvContacts.adapter = adapter

        // Set layout manager to position the items
        rvContacts.layoutManager = LinearLayoutManager(context)

        return root
    }

}