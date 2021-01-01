package com.rebellionandroid.components.entityUi.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.rebellionandroid.components.entityUi.R

class DialogModalFragment: DialogFragment() {

    private lateinit var positiveBtn: Button
    private lateinit var negativeBtn: Button

    companion object {
        private const val TITLE_TEXT_KEY = "TITLE_TEXT_KEY"
        fun newInstance(titleText: String): DialogModalFragment {
            val bundle = bundleOf(TITLE_TEXT_KEY to titleText)
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

        return root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

}
