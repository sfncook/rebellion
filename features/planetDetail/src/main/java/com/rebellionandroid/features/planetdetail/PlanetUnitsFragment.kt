package com.rebellionandroid.features.planetdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rebllelionandroid.core.Utilities

class PlanetUnitsFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        val (imgId, colorId) = Utilities.getLoyaltyIconForPlanet(planet)
        return inflater.inflate(R.layout.fragment_planet_units, container, false)
    }
}