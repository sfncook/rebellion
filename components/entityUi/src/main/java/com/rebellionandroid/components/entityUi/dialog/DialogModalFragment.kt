package com.rebellionandroid.components.entityUi.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.rebellionandroid.components.entityUi.R
import com.rebellionandroid.components.entityUi.dialog.components.DialogComponentParamKeys
import com.rebellionandroid.components.entityUi.dialog.components.DialogComponentPersonnelDetailsFragment
import com.rebellionandroid.components.entityUi.dialog.components.DialogComponentTypes

class DialogModalFragment: DialogFragment() {

    private lateinit var positiveBtn: Button
    private lateinit var negativeBtn: Button

    companion object {
        private const val TITLE_TEXT_KEY = "TITLE_TEXT_KEY"
        fun newInstance(titleText: String, bundleOptions: Bundle): DialogModalFragment {
            val bundle = bundleOf(TITLE_TEXT_KEY to titleText)
            bundle.putAll(bundleOptions)
            val frag = DialogModalFragment()
            frag.arguments = bundle
            return frag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_dialog_modal, container, false)

        val titleText = root.findViewById<TextView>(R.id.dlg_title_text)
        positiveBtn = root.findViewById(R.id.dlg_positive_btn)
        negativeBtn = root.findViewById(R.id.dlg_negative_btn)

        titleText.text = arguments?.getString(TITLE_TEXT_KEY)

        positiveBtn.setOnClickListener {
            dismiss()
        }
        negativeBtn.setOnClickListener {
            dismiss()
        }

        loadComponents()

        return root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    private fun loadComponents() {
        val componentsToShow = arguments?.getStringArrayList(DialogComponentParamKeys.ComponentsToShow.value)!!
        componentsToShow.forEach { componentToShow ->
            when(componentToShow) {
                DialogComponentTypes.PersonnelDetails.value -> loadComponent(
                    DialogComponentPersonnelDetailsFragment.newInstance(requireArguments()),
                    "DialogComponentPersonnelDetailsFragment"
                )
            }
        }
    }

    private fun loadComponent(fragment: Fragment, tag: String) {
        childFragmentManager.beginTransaction().add(
            R.id.dlg_components_list,
            fragment,
            tag
        ).commit()
    }

}
