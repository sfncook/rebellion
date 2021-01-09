package com.rebellionandroid.features.planetdetail

import android.content.ClipData
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.commands.CommandUtilities
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.database.gamestate.Personnel
import com.rebllelionandroid.core.database.gamestate.ShipWithUnits
import com.rebllelionandroid.core.database.gamestate.enums.UnitType
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import kotlinx.coroutines.launch

class PlanetUnitsFragment : Fragment() {
    private var currentGameStateId: Long = 0
    private var selectedPlanetId: Long = 0
    private lateinit var gameStateViewModel: GameStateViewModel
    private lateinit var listUnitsOnPlanetSurface: RecyclerView
    private lateinit var listShipsWithUnits: RecyclerView
    private lateinit var listEnemyShips: RecyclerView

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

        listUnitsOnPlanetSurface = root.findViewById(R.id.list_units_on_planet_surface)
        listShipsWithUnits = root.findViewById(R.id.list_ships_with_units)
        listEnemyShips = root.findViewById(R.id.list_enemy_ships)

        enemyUnitsSpecOpsImg = root.findViewById(R.id.enemy_units_specops_on_planet_img)
        manyEnemyUnitsSpecOpsTxt = root.findViewById(R.id.many_enemy_units_specops_on_planet)
        enemyUnitsGarisonImg = root.findViewById(R.id.enemy_units_garison_on_planet_img)
        manyEnemyUnitsGarisonTxt = root.findViewById(R.id.many_enemy_units_garison_on_planet)

        gameStateViewModel = (activity as BaseActivity).gameStateViewModel

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
                    val unitId = dragData.toString().toLong()
                    CommandUtilities.moveUnitToPlanetSurface(
                        gameStateViewModel,
                        unitId,
                        selectedPlanetId,
                        currentGameStateId
                    )
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameStateViewModel.gameState.observe(viewLifecycleOwner , { gameStateWithSectors ->
            val myTeam = gameStateWithSectors.gameState.myTeam
            gameStateViewModel.getPlanetWithUnits(selectedPlanetId) { planetWithUnits ->
                viewLifecycleOwner.lifecycleScope.launch {
                    // **** Enemy Units ****
                    var manyEnemyUnitsSpecOps = 0
                    var manyEnemyUnitsGarison = 0
                    planetWithUnits.personnels.forEach{ unit ->
                        if(unit.team != myTeam) {
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
                    when(myTeam) {
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
                updateUnitsOnShips(planetWithUnits.shipsWithUnits, gameStateWithSectors.gameState.myTeam)
            }

            gameStateViewModel.getAllUnitsOnTheSurfaceOfPlanet(selectedPlanetId) { units ->
                val myUnits = units.filter { unit -> unit.team == myTeam}
                updateUnitsOnPlanetSurface(myUnits)
            }
        })
    }

    override fun onResume() {
        super.onResume()

        gameStateViewModel.getPlanetWithUnits(selectedPlanetId) { planetWithUnits ->
            val planet = planetWithUnits.planet
            val sectorId = planet.sectorId
            val sector = gameStateViewModel.getSector(sectorId)
            viewLifecycleOwner.lifecycleScope.launch {
                val planetName = planet.name
                val sectorName = sector.name
                (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Planet: $planetName"
                (activity as AppCompatActivity?)!!.supportActionBar!!.subtitle =
                    "Sector: $sectorName"
                (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayShowHomeEnabled(true)
            }
        }

        val gameStateSharedPrefFile = getString(R.string.gameStateSharedPrefFile)
        val keyCurrentGameId = getString(R.string.keyCurrentGameId)
        val sharedPref = activity?.getSharedPreferences(gameStateSharedPrefFile, Context.MODE_PRIVATE)
        if(sharedPref?.contains(keyCurrentGameId) == true) {
            currentGameStateId = sharedPref.getLong(keyCurrentGameId, 0)
        } else {
            println("ERROR No current game ID found in shared preferences")
        }
    }

    private fun updateUnitsOnPlanetSurface(personnels: List<Personnel>) {
        val viewAdapter = UnitListAdapter(personnels, false)
        viewLifecycleOwner.lifecycleScope.launch {
            listUnitsOnPlanetSurface.adapter = viewAdapter
        }
    }

    private fun updateUnitsOnShips(shipsWithUnits: List<ShipWithUnits>, myTeam: TeamLoyalty) {
        val _this = this
        viewLifecycleOwner.lifecycleScope.launch {
            val myShipsWithUnits = shipsWithUnits.filter { shipWithUnits -> shipWithUnits.ship.team == myTeam }
            val myShipsViewAdapter = ShipsWithUnitListAdapter(myShipsWithUnits, gameStateViewModel, currentGameStateId, _this)
            listShipsWithUnits.adapter = myShipsViewAdapter

            val enemyShipsWithUnits = shipsWithUnits.filter { shipWithUnits -> shipWithUnits.ship.team != myTeam }
            if(enemyShipsWithUnits.isEmpty()) {
                listEnemyShips.visibility = GONE
            } else {
                listEnemyShips.visibility = VISIBLE
                val enemyShipsViewAdapter = EnemyShipsListAdapter(enemyShipsWithUnits)
                listEnemyShips.adapter = enemyShipsViewAdapter
            }
        }
    }
}
