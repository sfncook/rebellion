package com.rebellionandroid.components.entityUi.dialog.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.rebellionandroid.components.entityUi.R

class DialogComponentPersonnelDetailsFragment(): DialogFragment() {

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

        return root
    }
}
