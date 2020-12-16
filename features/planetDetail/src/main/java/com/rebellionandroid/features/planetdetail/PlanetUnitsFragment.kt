package com.rebellionandroid.features.planetdetail

import android.content.ClipData
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.ShipWithUnits
import com.rebllelionandroid.core.database.gamestate.Unit
import com.rebllelionandroid.core.database.gamestate.enums.UnitType
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
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

    private lateinit var enemyUnitsSpecOpsImg: ImageView
    private lateinit var manyEnemyUnitsSpecOpsTxt: TextView
    private lateinit var enemyUnitsGarisonImg: ImageView
    private lateinit var manyEnemyUnitsGarisonTxt: TextView

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
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

        enemyUnitsSpecOpsImg = root.findViewById(R.id.enemy_units_specops_on_planet_img)
        manyEnemyUnitsSpecOpsTxt = root.findViewById(R.id.many_enemy_units_specops_on_planet)
        enemyUnitsGarisonImg = root.findViewById(R.id.enemy_units_garison_on_planet_img)
        manyEnemyUnitsGarisonTxt = root.findViewById(R.id.many_enemy_units_garison_on_planet)

        gameStateViewModel = (activity as BaseActivity).gameStateViewModel
        val gameStateWithSectors = gameStateViewModel.gameState
        gameStateWithSectors.observe(viewLifecycleOwner , { gameStateWithSectors ->
            gameStateViewModel.getPlanetWithUnits(selectedPlanetId) { planetWithUnits ->
                val planet = planetWithUnits.planet
                viewLifecycleOwner.lifecycleScope.launch {
                    textLoyaltyPercTeamA.text = "${planet.teamALoyalty.toString()}%"
                    textLoyaltyPercTeamB.text = "${planet.teamBLoyalty.toString()}%"
                    val (imgId, colorId) = Utilities.getLoyaltyIconForPlanet(planet)
                    planetLoyaltyImg.setImageResource(imgId)
                    planetLoyaltyImg.setColorFilter(
                        ContextCompat.getColor(root.context, colorId), android.graphics.PorterDuff.Mode.MULTIPLY
                    )

                    // **** Enemy Units ****
                    var manyEnemyUnitsSpecOps = 0
                    var manyEnemyUnitsGarison = 0
                    planetWithUnits.units.forEach{unit ->
                        if(unit.team != gameStateWithSectors.gameState.myTeam) {
                            when (unit.unitType) {
                                UnitType.SpecialForces -> manyEnemyUnitsSpecOps =
                                    manyEnemyUnitsSpecOps.inc()
                                UnitType.Garrison -> manyEnemyUnitsGarison =
                                    manyEnemyUnitsGarison.inc()
                                else -> {
                                }
                            }
                        }
                    }
                    manyEnemyUnitsSpecOpsTxt.text = manyEnemyUnitsSpecOps.toString()
                    manyEnemyUnitsGarisonTxt.text = manyEnemyUnitsGarison.toString()

                    var enemyColor = 0
                    when(gameStateWithSectors.gameState.myTeam) {
                        TeamLoyalty.TeamA -> enemyColor = R.color.loyalty_team_b
                        TeamLoyalty.TeamB -> enemyColor = R.color.loyalty_team_a
                        else -> {}
                    }
                    enemyUnitsSpecOpsImg.setColorFilter(
                        ContextCompat.getColor(requireContext(), enemyColor), android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    enemyUnitsGarisonImg.setColorFilter(
                        ContextCompat.getColor(requireContext(), enemyColor), android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    // **** Enemy Units ****
                }
                updateUnitsOnShips(planetWithUnits.shipsWithUnits)
            }

            gameStateViewModel.getAllUnitsOnTheSurfaceOfPlanet(selectedPlanetId) {
                updateUnitsOnPlanetSurface(it)
            }
        })

        val dragListen = View.OnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_ENTERED -> {
                    v.findViewById<View>(R.id.list_units_on_planet_surface).setBackgroundColor(Color.GREEN)
                    v.invalidate()
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    v.findViewById<View>(R.id.list_units_on_planet_surface).setBackgroundColor(
                        ContextCompat.getColor(v.context, R.color.list_item_bg)
                    )
                    v.invalidate()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    val item: ClipData.Item = event.clipData.getItemAt(0)
                    val dragData = item.text
                    gameStateViewModel.moveUnitToPlanet(dragData.toString().toLong(), selectedPlanetId, currentGameStateId)
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    v.findViewById<View>(R.id.list_units_on_planet_surface).setBackgroundColor(
                        ContextCompat.getColor(v.context, R.color.list_item_bg)
                    )
                    v.invalidate()
                    true
                }
                else -> true
            }
        }
        listUnitsOnPlanetSurface.setOnDragListener(dragListen)

        return root
    }

    override fun onResume() {
        super.onResume()

        val gameStateSharedPrefFile = getString(R.string.gameStateSharedPrefFile)
        val keyCurrentGameId = getString(R.string.keyCurrentGameId)
        val sharedPref = activity?.getSharedPreferences(gameStateSharedPrefFile, Context.MODE_PRIVATE)
        if(sharedPref?.contains(keyCurrentGameId) == true) {
            currentGameStateId = sharedPref.getLong(keyCurrentGameId, 0)
        } else {
            println("ERROR No current game ID foudn in shared preferences")
        }
    }

    private fun updateUnitsOnPlanetSurface(units: List<Unit>) {
        val viewAdapter = UnitListAdapter(units, false)
        viewLifecycleOwner.lifecycleScope.launch {
            listUnitsOnPlanetSurface.adapter = viewAdapter
        }
    }

    private fun updateUnitsOnShips(shipsWithUnits: List<ShipWithUnits>) {
        val viewAdapter = ShipsWithUnitListAdapter(shipsWithUnits, gameStateViewModel, currentGameStateId)
        viewLifecycleOwner.lifecycleScope.launch {
            listShipsWithUnits.adapter = viewAdapter
        }
    }
}
