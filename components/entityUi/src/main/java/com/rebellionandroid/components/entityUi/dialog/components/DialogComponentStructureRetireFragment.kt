package com.rebellionandroid.components.entityUi.dialog.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.rebellionandroid.components.entityUi.R
import com.rebellionandroid.components.entityUi.dialog.DialogModalFragment
import com.rebllelionandroid.core.BaseActivity
import com.rebllelionandroid.core.Utilities

class DialogComponentStructureRetireFragment(): DialogFragment() {

    private var structureId: Long? = null
    private var currentGameStateId: Long? = null

    companion object {
        fun newInstance(bundleOptions: Bundle): DialogComponentStructureRetireFragment {
            val frag = DialogComponentStructureRetireFragment()
            frag.arguments = bundleOptions
            return frag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dialog_component_retire_structure, container, false)


        val gameStateViewModel = (activity as BaseActivity).gameStateViewModel

        structureId = arguments?.getLong(DialogComponentParamKeys.StructureId.value)

        val retireStructureBtn = root.findViewById<TextView>(R.id.dlgcomp_structure_retire_btn)
        retireStructureBtn.setOnClickListener {
            if(currentGameStateId!=null && structureId!=null) {
                gameStateViewModel.retireStructure(currentGameStateId!!, structureId!!)
                if(parentFragment!=null && parentFragment!!::class == DialogModalFragment::class) {
                    (parentFragment as DialogModalFragment).onDimiss()
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
