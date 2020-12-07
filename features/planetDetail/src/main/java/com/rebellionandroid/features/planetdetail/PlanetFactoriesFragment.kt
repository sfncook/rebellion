package com.rebellionandroid.features.planetdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.database.gamestate.Factory
import kotlinx.coroutines.launch

class PlanetFactoriesFragment : Fragment() {
    private var selectedPlanetId: Long = 0
    private lateinit var listFactories: RecyclerView
    private lateinit var listEmptyEnergies: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_planet_factories, container, false)
        selectedPlanetId = arguments?.getLong("planetId")!!
        listFactories = root.findViewById(R.id.list_factories)
        listEmptyEnergies = root.findViewById(R.id.list_empty_energy)

        val gameStateViewModel = (activity as BaseActivity).gameStateViewModel
        val gameStateWithSectors = gameStateViewModel.gameState
        gameStateWithSectors.observe(viewLifecycleOwner , {
            gameStateViewModel.getPlanetWithUnits(selectedPlanetId) { planetWithUnits ->
                updateFactoriesList(planetWithUnits.factories)
                val manyEmptyEnergies = planetWithUnits.planet.energyCap - planetWithUnits.factories.size
                println("manyEmptyEnergies: $manyEmptyEnergies")
                updateEmptyEnergyList(manyEmptyEnergies)
            }

        })

        return root
    }

    private fun updateFactoriesList(factories: List<Factory>) {
        val viewAdapter = FactoryListAdapter(factories)
        viewLifecycleOwner.lifecycleScope.launch {
            listFactories.adapter = viewAdapter
        }
    }

    private fun updateEmptyEnergyList(manyEmptyEnergies: Int) {
        val emptyEnergies = (IntArray(manyEmptyEnergies) { it }).toCollection(ArrayList())
        val viewAdapter = EmptyEnergyListAdapter(emptyEnergies)
        viewLifecycleOwner.lifecycleScope.launch {
            listEmptyEnergies.adapter = viewAdapter
        }
    }
}
