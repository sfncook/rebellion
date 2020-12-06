package com.rebellionandroid.features.planetdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.GameStateWithSectors
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.gamestate.Unit
import kotlinx.coroutines.launch

class PlanetUnitsFragment : Fragment() {
    private var currentGameStateId: Long = 0
    private var selectedPlanetId: Long = 0
    private lateinit var gameStateViewModel: GameStateViewModel
    private lateinit var textLoyaltyPercTeamA: TextView
    private lateinit var textLoyaltyPercTeamB: TextView
    private lateinit var planetLoyaltyImg: ImageView
    private lateinit var listUnitsOnPlanetSurface: RecyclerView
    private lateinit var listShipsWithUnits: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_planet_units, container, false)
        selectedPlanetId = arguments?.getLong("planetId")!!

        textLoyaltyPercTeamA = root.findViewById(R.id.units_text_loyalty_perc_team_a)
        textLoyaltyPercTeamB = root.findViewById(R.id.units_text_loyalty_perc_team_b)
        planetLoyaltyImg = root.findViewById(R.id.units_planet_loyalty)
        listUnitsOnPlanetSurface = root.findViewById(R.id.list_units_on_planet_surface)
        listShipsWithUnits = root.findViewById(R.id.list_ships_with_units)

        gameStateViewModel = (activity as BaseActivity).gameStateViewModel
        val gameStateWithSectors = gameStateViewModel.gameState
        gameStateWithSectors.observe(viewLifecycleOwner , {
            gameStateViewModel.getPlanetWithUnits(selectedPlanetId) {
                val planet = it.planet
                viewLifecycleOwner.lifecycleScope.launch {
                    textLoyaltyPercTeamA.text = "${planet.teamALoyalty.toString()}%"
                    textLoyaltyPercTeamB.text = "${planet.teamBLoyalty.toString()}%"
                    val (imgId, colorId) = Utilities.getLoyaltyIconForPlanet(planet)
                    planetLoyaltyImg.setImageResource(imgId)
                    planetLoyaltyImg.setColorFilter(
                        ContextCompat.getColor(root.context, colorId), android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                }
            }

            gameStateViewModel.getAllUnitsOnTheSurfaceOfPlanet(selectedPlanetId) {
                updateUnitsOnPlanetSurface(it)
            }
        })

        return root
    }

    private fun updateUnitsOnPlanetSurface(units: List<Unit>) {
        val viewAdapter = UnitListAdapter(units)
        viewLifecycleOwner.lifecycleScope.launch {
            listUnitsOnPlanetSurface.adapter = viewAdapter
        }
    }
}
