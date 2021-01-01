package com.rebellionandroid.components.entityUi.dialog.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.rebellionandroid.components.entityUi.R
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.enums.MissionTargetType

class DialogComponentPersonnelDetailsFragment(): DialogFragment() {

    private var personnelId: Long? = null
    private var currentGameStateId: Long? = null

    companion object {
        fun newInstance(bundleOptions: Bundle): DialogComponentPersonnelDetailsFragment {
            val frag = DialogComponentPersonnelDetailsFragment()
            frag.arguments = bundleOptions
            return frag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dialog_component_personnel_details, container, false)

        val personnelTypeText = root.findViewById<TextView>(R.id.dlgcomp_personnel_type)
        val personnelMissionTypeText = root.findViewById<TextView>(R.id.dlgcomp_personnel_mission_type)
        val personnelMissionTargetText = root.findViewById<TextView>(R.id.dlgcomp_personnel_mission_target)
        val personnelMissionEtaText = root.findViewById<TextView>(R.id.dlgcomp_personnel_mission_eta)
        val cancelMissionBtn = root.findViewById<TextView>(R.id.dlgcomp_personnel_mission_cancel_btn)

        val gameStateViewModel = (activity as BaseActivity).gameStateViewModel

        cancelMissionBtn.setOnClickListener {
            if(currentGameStateId!=null && personnelId!=null) {
                gameStateViewModel.cancelMission(currentGameStateId!!, personnelId!!)
                dismiss()
            }
        }

        personnelId = arguments?.getLong(DialogComponentParamKeys.PersonnelId.value)
        if(personnelId!=null) {
            gameStateViewModel.getPersonnel(personnelId!!) { personnel ->
                personnelTypeText.text = personnel.unitType.value
                if(personnel.missionType!=null) {
                    personnelMissionTypeText.text = personnel.missionType!!.value
                }

                if(personnel.dayMissionComplete!=null) {
                    personnelMissionEtaText.text = personnel.dayMissionComplete.toString()
                }

                if(personnel.missionTargetType!=null && personnel.missionTargetId!=null) {
                    when(personnel.missionTargetType) {
                        MissionTargetType.DefenseStructure -> {
                            gameStateViewModel.getDefenseStructure(personnel.missionTargetId!!) {
                                personnelMissionTargetText.text = it.defenseStructureType.value
                            }
                        }

                        MissionTargetType.Ship -> {
                            gameStateViewModel.getShip(personnel.missionTargetId!!) {
                                personnelMissionTargetText.text = it.shipType.value
                            }
                        }

                        MissionTargetType.Factory -> {
                            gameStateViewModel.getFactory(personnel.missionTargetId!!) {
                                personnelMissionTargetText.text = it.factoryType.value
                            }
                        }

                        MissionTargetType.Planet -> {
                            gameStateViewModel.getPlanet(personnel.missionTargetId!!) {
                                personnelMissionTargetText.text = it.name
                            }
                        }

                        MissionTargetType.Unit -> {
                            gameStateViewModel.getPersonnel(personnel.missionTargetId!!) {
                                personnelMissionTargetText.text = it.unitType.value
                            }
                        }
                    }
                }
            }
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        currentGameStateId = Utilities.getCurrentGameStateId(
            getString(R.string.gameStateSharedPrefFile),
            getString(R.string.keyCurrentGameId),
            requireActivity()
        )
    }
}
