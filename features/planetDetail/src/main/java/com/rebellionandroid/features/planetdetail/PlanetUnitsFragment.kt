package com.rebellionandroid.features.planetdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel

class PlanetUnitsFragment : Fragment() {
    private var currentGameStateId: Long = 0
    private var selectedPlanetId: Long = 0
    private lateinit var gameStateViewModel: GameStateViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_planet_units, container, false)
        selectedPlanetId = arguments?.getLong("planetId")!!

        gameStateViewModel = (activity as BaseActivity).gameStateViewModel
        val gameStateWithSectors = gameStateViewModel.gameState
        gameStateWithSectors.observe(viewLifecycleOwner , {
            val planetWithUnits = gameStateViewModel.getPlanetWithUnits(selectedPlanetId) {

            }
//            updatePlanetDetail(planetWithUnits)
        })

        return root
    }
}
