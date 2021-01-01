package com.rebellionandroid.components.entityUi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class DialogComponentPersonnelDetailsFragment(): DialogFragment() {

    companion object {
        private const val TITLE_TEXT_KEY = "TITLE_TEXT_KEY"
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

        return root
    }
}