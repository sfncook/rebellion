package com.rebellionandroid.components.entityUi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.gameUpdater.uprising.UprisingRank
import kotlinx.coroutines.launch

class PlanetStatusComponentFragment : Fragment() {
    private var currentGameStateId: Long? = null
    private var planetId: Long? = null
    private lateinit var gameStateViewModel: GameStateViewModel
    private lateinit var textLoyaltyPercTeamA: TextView
    private lateinit var textLoyaltyPercTeamB: TextView
    private lateinit var planetLoyaltyImg: ImageView
    private lateinit var inConflictImg: ImageView
    private lateinit var insurrectionRatingCivilImg: ImageView
    private lateinit var insurrectionRatingUnrestImg: ImageView
    private lateinit var insurrectionRatingUprisingImg: ImageView
    private lateinit var insurrectionRatingRebellionImg: ImageView
    private lateinit var energyList: LinearLayout

    companion object {
        fun newInstance(planetId: Long): PlanetStatusComponentFragment {
            val bundle = bundleOf("planetId" to planetId)
            val frag = PlanetStatusComponentFragment()
            frag.arguments = bundle
            return frag
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_planet_status_component, container, false)
        planetId = arguments?.getLong("planetId")!!

        gameStateViewModel = (activity as BaseActivity).gameStateViewModel

        planetLoyaltyImg = root.findViewById(R.id.planet_status_planet_loyalty)
        textLoyaltyPercTeamA = root.findViewById(R.id.planet_status_text_loyalty_perc_team_a)
        textLoyaltyPercTeamB = root.findViewById(R.id.planet_status_text_loyalty_perc_team_b)
        inConflictImg = root.findViewById(R.id.planet_status_inconflict_img)
        insurrectionRatingCivilImg = root.findViewById(R.id.planet_status_insurrection_civil_img)
        insurrectionRatingUnrestImg = root.findViewById(R.id.planet_status_insurrection_unrest_img)
        insurrectionRatingUprisingImg = root.findViewById(R.id.planet_status_insurrection_uprising_img)
        insurrectionRatingRebellionImg = root.findViewById(R.id.planet_status_insurrection_rebellion_img)
        energyList = root.findViewById(R.id.planet_energy_imgs_list)

        return root
    }

    override fun onResume() {
        super.onResume()

        currentGameStateId = Utilities.getCurrentGameStateId(
            getString(R.string.gameStateSharedPrefFile),
            getString(R.string.keyCurrentGameId),
            requireActivity()
        )

        gameStateViewModel.gameState.observe(viewLifecycleOwner , { gameStateWithSectors ->
            gameStateViewModel.getPlanetWithUnits(planetId!!) { planetWithUnits ->
                val planet = planetWithUnits.planet
                viewLifecycleOwner.lifecycleScope.launch {
                    textLoyaltyPercTeamA.text = "${planet.teamALoyalty.toString()}%"
                    textLoyaltyPercTeamB.text = "${planet.teamBLoyalty.toString()}%"
                    inConflictImg.visibility = if(planet.inConflict) View.VISIBLE else View.GONE

                    inConflictImg.visibility = View.GONE
                    insurrectionRatingCivilImg.visibility = View.GONE
                    insurrectionRatingUnrestImg.visibility = View.GONE
                    insurrectionRatingUprisingImg.visibility = View.GONE
                    insurrectionRatingRebellionImg.visibility = View.GONE
                    when(planet.uprisingRank) {
                        UprisingRank.Civil -> insurrectionRatingCivilImg.visibility = View.VISIBLE
                        UprisingRank.Unrest -> insurrectionRatingUnrestImg.visibility = View.VISIBLE
                        UprisingRank.Uprising -> insurrectionRatingUprisingImg.visibility = View.VISIBLE
                        UprisingRank.Rebellion -> insurrectionRatingRebellionImg.visibility = View.VISIBLE
                    }


                    val (imgId, colorId) = Utilities.getLoyaltyIconForPlanet(planet)
                    planetLoyaltyImg.setImageResource(imgId)
                    planetLoyaltyImg.setColorFilter(
                        ContextCompat.getColor(requireContext(), colorId), android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    Utilities.populateEnergiesUi(requireContext(), energyList, planetWithUnits)
                }
            }
        })
    }
}
