package com.rebellionandroid.features.sectorslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.database.gamestate.GameStateWithSectors
import com.rebllelionandroid.features.sectorsList.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SectorsListFragment: Fragment() {

    private var currentGameStateId: Long = 0
    private lateinit var gameStateWithSectors: LiveData<GameStateWithSectors>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_sectors_list, container, false)

        val gameStateViewModel = (activity as BaseActivity).gameStateViewModel
        gameStateWithSectors = gameStateViewModel.gameState
        gameStateWithSectors.observe(viewLifecycleOwner , {
            root.findViewById<TextView>(R.id.sectorlist_text).text = it.gameState.id.toString()
        })

//        root.findViewById<MaterialButton>(R.id.btn_goto_detail).setOnClickListener {
//            root.findNavController().navigate(R.id.third_graph)
//        }
        val gameStateSharedPrefFile = getString(R.string.gameStateSharedPrefFile)
        val keyCurrentGameId = getString(R.string.keyCurrentGameId)
        val sharedPref = activity?.getSharedPreferences(gameStateSharedPrefFile, Context.MODE_PRIVATE)
        if(sharedPref?.contains(keyCurrentGameId) == true) {
            currentGameStateId = sharedPref.getLong(keyCurrentGameId, 0)
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                gameStateViewModel.loadAllGameStateWithSectors(currentGameStateId)
            }
        } else {
            println("ERROR No current game ID found in shared preferences")
        }

        return root
    }

}