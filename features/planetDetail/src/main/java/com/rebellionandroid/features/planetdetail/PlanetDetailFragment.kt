package com.rebellionandroid.features.planetdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class PlanetDetailFragment: Fragment() {
    private var selectedPlanetId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_planet_detail, container, false)
        selectedPlanetId = arguments?.getLong("planetId")!!

        val planetUnitsFragment = PlanetUnitsFragment()
        val planetFactoriesFragment = PlanetFactoriesFragment()

        // Init Units view
        loadUnitsFrag(planetUnitsFragment, "PlanetUnitsFragment")

        val bottomNavigation: BottomNavigationView = root.findViewById(R.id.planet_detail_bottom_nav)
//        BottomNavigationView.OnNavigationItemSelectedListener { item ->
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_planet_units -> {
                    println("click units")
                    loadUnitsFrag(planetUnitsFragment, "PlanetUnitsFragment")
                    true
                }
                R.id.navigation_planet_factories -> {
                    println("click factories")
                    loadUnitsFrag(planetFactoriesFragment, "PlanetFactoriesFragment")
                    true
                }
                else -> false
            }
        }

        return root
    }

    private fun loadUnitsFrag(fragment: Fragment, tag: String) {
        val bundle = bundleOf("planetId" to selectedPlanetId)
        fragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.planet_detail_body, fragment, tag)
            .addToBackStack(null)
            .commit()
    }
}